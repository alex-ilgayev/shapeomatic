package com.shapeomatic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shapeomatic.Controller.DAL;
import com.shapeomatic.Controller.Settings;
import com.shapeomatic.Model.User;
import com.shapeomatic.Networking.IRestAPI;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EndGameActivity extends Activity {

    public static final String TAG_FINAL_SCORE = "Score";

    private TextView mTvScore;
    private TextView mTvHighScore;
    private Typeface mFont;
    private int mScore;
    private int mWidth;
    private int mHeight;
    private SharedPreferences mPrefs;

    private Retrofit mRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        mFont = Typeface.createFromAsset(getAssets(), "fonts/YehudaCLM-Light.ttf");

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Settings.BASE_PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mScore = (int) getIntent().getExtras().get(TAG_FINAL_SCORE);
        mPrefs = getSharedPreferences(GameActivity.PREF_TAG, MODE_PRIVATE);
        mTvScore = (TextView) findViewById(R.id.tvLevel);
        mTvScore.setTypeface(mFont);
        mTvScore.setText(String.valueOf(mScore));
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        // updating the score
        int highScore;
        final User me = DAL.getInstance().getUser(mPrefs, GameActivity.PREF_TAG_USER);
        if(mScore > me.getScore()) {
            me.setScore(mScore);
            DAL.getInstance().putUser(mPrefs, GameActivity.PREF_TAG_USER, me);

        }

        // updating the user remotely
        // checks if user needs to be created, if yes then creting, if no then updating.
        if(!me.getIsCreated())
            createCurrentUserAtServer();
        else
            updateCurrentUserAtServer();

        highScore = me.getScore();

        mTvHighScore = (TextView) findViewById(R.id.tvBestScore);
        mTvHighScore.setTypeface(mFont);
        mTvHighScore.setText(String.valueOf(highScore));
    }

    private void createCurrentUserAtServer() {
        final User user = DAL.getInstance().getUser(mPrefs, GameActivity.PREF_TAG_USER);

        IRestAPI service = mRetrofit.create(IRestAPI.class);
        Call<Void> reportCall = service.createUser(user);
        reportCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    try {
                        Log.e(Settings.TAG, "server internal error: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                user.setUserCreated();
                DAL.getInstance().putUser(mPrefs, GameActivity.PREF_TAG_USER, user);
                Log.i(Settings.TAG, "User Was created successfully (Remote)");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(Settings.TAG, "can't reach server: " + t.toString());
            }
        });
    }

    private void updateCurrentUserAtServer() {
        final User user = DAL.getInstance().getUser(mPrefs, GameActivity.PREF_TAG_USER);

        IRestAPI service = mRetrofit.create(IRestAPI.class);
        Call<Void> reportCall = service.updateUser(user.getFacebookId(), user);
        reportCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    try {
                        Log.e(Settings.TAG, "server internal error: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                Log.i(Settings.TAG, "User Was update successfully (Remote)");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(Settings.TAG, "can't reach server: " + t.toString());
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mHeight == 0)
            mHeight = getWindow().getDecorView().getHeight();
        if(mWidth == 0)
            mWidth = getWindow().getDecorView().getWidth();

        if(event.getX() < 0 || event.getX() > mWidth || event.getY() < 0 || event.getY() > mHeight)
            return false;

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }

    public void onClickRestart(View v) {
        Intent i = new Intent(this, GameActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(this, GameActivity.class);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(setIntent);
    }

    public void onClickShare(View v) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        String shareTitle = getResources().getString(R.string.share_title);
        String shareBody = getResources().getString(R.string.share_text) + " " + mScore + " " +
                getResources().getString(R.string.share_text_2);
        String shareSubject = getResources().getString(R.string.share_subject);

        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        startActivity(Intent.createChooser(sharingIntent, shareTitle));
    }
}