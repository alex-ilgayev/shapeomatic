package com.shapeomatic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shapeomatic.Controller.DAL;
import com.shapeomatic.Controller.Settings;

import com.facebook.FacebookSdk;
import com.shapeomatic.Model.User;
import com.shapeomatic.Networking.IRestAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameActivity extends AppCompatActivity {
    public static final String PREF_TAG = "shapeomaticPref";
    public static final String PREF_TAG_USER = "user";

    GameView mainView;

    SharedPreferences mPrefs;

    CallbackManager mCallbackManager;

    ImageView mBtnLeaderboard;
    ImageView mBtnFacebookLogin;
    ImageView mBtnFacebookLogout;

    Retrofit mRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_game);

        mainView = (GameView) findViewById(R.id.mainView);
        mBtnFacebookLogin = (ImageView) findViewById(R.id.btnFacebookLogin);
        mBtnFacebookLogout = (ImageView) findViewById(R.id.btnFacebookLogout);
        mBtnLeaderboard = (ImageView) findViewById(R.id.btnLeaderboard);

        mPrefs = getSharedPreferences(GameActivity.PREF_TAG, Context.MODE_PRIVATE);

        // creating first time local user (if doesn't exists)
        User user = DAL.getInstance().getUser(mPrefs, PREF_TAG_USER);
        if(user == null) {
            user = new User();
            DAL.getInstance().putUser(mPrefs, PREF_TAG_USER, user);
        }

        AppEventsLogger.activateApp(this);

        // setting facebook buttons
        mBtnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(GameActivity.this,
                        Arrays.asList("email", "user_friends", "public_profile"));
            }
        });

        mBtnFacebookLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
            }
        });

        // setting facebook login callback
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(GameActivity.this, "SUCCESS", Toast.LENGTH_LONG).show();
                Log.i(Settings.TAG, "Facebook Login Success");
            }

            @Override
            public void onCancel() {
                Log.i(Settings.TAG, "Facebook cancel");
                Toast.makeText(GameActivity.this, "CANCEL", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(GameActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                Log.e(Settings.TAG, "Facebook Exception: " + error.getCause().toString());
            }
        });

        // setting facebook login/logout tracker
        AccessTokenTracker tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                // logout
                if(currentAccessToken == null) {
                    onFacebookLogout();
                }
                // login
                else {
                    onFacebookLogin();
                }
            }
        };
        tracker.startTracking();

        // checking initial state.
        if(isFacebookLoggedIn()) {
            onFacebookLogin();
        }
        else {
            onFacebookLogout();
        }

        mBtnLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GameActivity.this, LeaderboardActivity.class);
                startActivity(i);
            }
        });

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Settings.BASE_PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private void onFacebookLogin() {
        Log.i(Settings.TAG, "Facebook login");
        mBtnFacebookLogin.setVisibility(View.INVISIBLE);
        mBtnFacebookLogout.setVisibility(View.VISIBLE);

        User user = DAL.getInstance().getUser(mPrefs, PREF_TAG_USER);
        if(!user.getIsFacebookSynced())
            sync();
    }

    private void onFacebookLogout() {
        Log.d(Settings.TAG, "Facebook logout");
        mBtnFacebookLogout.setVisibility(View.INVISIBLE);
        mBtnFacebookLogin.setVisibility(View.VISIBLE);

        User user = DAL.getInstance().getUser(mPrefs, PREF_TAG_USER);
        user.setIsFacebookSynced(false);
        DAL.getInstance().putUser(mPrefs, PREF_TAG_USER, user);
    }

    private void sync() {
        syncMeFromFacebook();
    }

    // retrieves personal information from facebook servers.
    // done only when signing to faceobok. (one-time)
    private void syncMeFromFacebook() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if(response.getError() != null) {
                    Toast.makeText(GameActivity.this, "Facebook connection failed.", Toast.LENGTH_LONG);
                    Log.e(Settings.TAG, "Get User Information from Facebook Failed");
                    Log.e(Settings.TAG, response.getError().getErrorMessage());
                    return;
                }
                long facebookId;
                String facebookName;
                try {
                    facebookId = object.getLong("id");
                    facebookName = object.getString("name");

                } catch(JSONException e) {
                    Log.e(Settings.TAG, e.getMessage());
                    return;
                }

                Log.i(Settings.TAG, "Sync me (Facebook) completed.");

                User currentUser = DAL.getInstance().getUser(mPrefs, PREF_TAG_USER);
                User user = new User(facebookId, facebookName, null, currentUser.getScore(), null, false, true);
                DAL.getInstance().putUser(mPrefs, PREF_TAG_USER, user);

                syncFriendsFromFacebook();
                syncPicFromFacebook(user);
                getUserFromServer();
            }
        });
        Bundle parameters = new Bundle();
        request.setParameters(parameters);
        request.executeAsync();
    }

    // retrieves url of the profile pic from facebook.
    // returns url.
    public void syncPicFromFacebook(User user) {
        GraphRequest request = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(), "me/picture", new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                if (response.getError() != null) {
                    Toast.makeText(GameActivity.this, "Facebook connection failed.", Toast.LENGTH_LONG);
                    Log.e(Settings.TAG, "Get User Pic from Facebook Failed");
                    Log.e(Settings.TAG, response.getError().getErrorMessage());
                    return;
                }
                String picUrl;
                try {
                    picUrl = ((JSONObject) response.getJSONObject().get("data")).getString("url");

                } catch(JSONException e) {
                    Log.e(Settings.TAG, e.getMessage());
                    return;
                }
                Log.i(Settings.TAG, "Get User Pic from Facebook Successful");

                User me = DAL.getInstance().getUser(mPrefs, PREF_TAG_USER);
                me.setPic(picUrl);
                DAL.getInstance().putUser(mPrefs, PREF_TAG_USER, me);
            }
        });
        Bundle parameters = new Bundle();
        parameters.putBoolean("redirect", false);
        request.setParameters(parameters);
        request.executeAsync();
    }

    // retrieves friends information from facebook servers.
    private void syncFriendsFromFacebook() {
        GraphRequest request = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(), "me/friends", new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                if (response.getError() != null) {
                    Toast.makeText(GameActivity.this, "Facebook connection failed.", Toast.LENGTH_LONG);
                    Log.e(Settings.TAG, "Get User Friends from Facebook Failed");
                    Log.e(Settings.TAG, response.getError().getErrorMessage());
                    return;
                }
//                Log.d(Settings.TAG, response.getJSONObject().toString());
                JSONArray arr;
                ArrayList<User> friendsList = new ArrayList<User>();
                try {
                    arr = (JSONArray) response.getJSONObject().get("data");
                    friendsList = new ArrayList<User>();
                    for(int i=0; i<arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        long facebookId = obj.getLong("id");
                        String facebookName = obj.getString("name");
                        User user = new User(facebookId, facebookName, null, (-1), null, false, false);
                        friendsList.add(user);
                    }
                } catch(JSONException e) {
                    Log.e(Settings.TAG, e.getMessage());
                    return;
                }
                Log.i(Settings.TAG, "Sync Friends (Facebook) Successful");

                User me = DAL.getInstance().getUser(mPrefs, PREF_TAG_USER);
                me.setFriends(friendsList);
                DAL.getInstance().putUser(mPrefs, PREF_TAG_USER, me);
            }
        });
        Bundle parameters = new Bundle();
        request.setParameters(parameters);
        request.executeAsync();
    }

    // retreived user information from remote server.
    // syncs local high score with the remote information.
    // called every time you starting app.
    private void getUserFromServer() {
        final User user = DAL.getInstance().getUser(mPrefs, PREF_TAG_USER);

        IRestAPI service = mRetrofit.create(IRestAPI.class);
        Call<User> reportCall = service.getUserById(user.getFacebookId());
        reportCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    try {
                        if(response.raw().code() == 404) { // user doesn't exists error.


                            Log.i(Settings.TAG, "tried to get user but it doesn't exists.");
                        }
                        else
                            Log.e(Settings.TAG, "server internal error: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                User returnedUser = response.body();
                if(returnedUser == null) {
                    Log.e(Settings.TAG, "the user received was null");
                    return;
                }

                // setting flag that user is already created.
                user.setUserCreated();

                user.setScore(returnedUser.getScore() > user.getScore() ?  returnedUser.getScore(): user.getScore());
//                user.setFriends(returnedUser.getFriends());
                DAL.getInstance().putUser(mPrefs, PREF_TAG_USER, user);

                Log.i(Settings.TAG, "Sync High Score Successful (Remote)");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(Settings.TAG, "can't reach server: " + t.toString());
            }
        });


//
//        new UserUpdater(GameActivity.this, new Long(user.getFacebookId()), UserUpdater.OperationKind.GET_USER)
//                .run(new UserUpdater.IResponseHandler() {
//                    @Override
//                    public void handleResponse(String response) {
//                        User parsedUser;
//                        try {
//                            parsedUser = EntityParser.xmlToUser(response);
//
//                            user.setScore(parsedUser.getScore() > user.getScore() ?  parsedUser.getScore(): user.getScore());
//                            user.setFriends(parsedUser.getFriends());
//                            DAL.getInstance().putUser(mPrefs, PREF_TAG_USER, user);
//
//                            Log.i(Settings.TAG, "Sync High Score Successful (Remote)");
//                        } catch(Exception e) {
//                            Log.e(Settings.TAG, "Error parsing user: " + e.getMessage());
//                            return;
//                        }
//                    }
//                });
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);
        mCallbackManager.onActivityResult(requestCode, responseCode, data);
    }

    @Override
    public void onBackPressed() {
        mainView.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();

        mainView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        mainView.onResume();
    }

    private boolean isFacebookLoggedIn() {
        AccessToken tok = AccessToken.getCurrentAccessToken();
        return tok != null;
    }
}
