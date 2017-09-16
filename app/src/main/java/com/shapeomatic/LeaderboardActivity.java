package com.shapeomatic;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shapeomatic.Controller.DAL;
import com.shapeomatic.Controller.Settings;
import com.shapeomatic.Model.User;
import com.shapeomatic.Networking.IRestAPI;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaderboardActivity extends AppCompatActivity {

    SharedPreferences mPrefs;

    TextView mTvLeaderboardTitle;
    ListView mLvLeaderboard;
    ProgressBar mPbLoading;

    LeaderboardAdapter mAdapter;
    List<User> mUsers;

    Retrofit mRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        mPrefs = getSharedPreferences(GameActivity.PREF_TAG, Context.MODE_PRIVATE);

        mUsers = new LinkedList<>();

//        User me = DAL.getInstance().getUser(mPrefs, GameActivity.PREF_TAG_USER);

//        mUsers.add(me);
//        if(me.getFriends() != null) {
//            for(User friend: me.getFriends()) {
//                mUsers.add(friend);
//            }
//        }

        mTvLeaderboardTitle = (TextView) findViewById(R.id.tvLeaderboardTitle);
        mLvLeaderboard = (ListView) findViewById(R.id.lvLeaderboard);
        mPbLoading = (ProgressBar) findViewById(R.id.pbLoading);

        mTvLeaderboardTitle.setTypeface(Settings.FONT);

        mAdapter = new LeaderboardAdapter(this, R.layout.list_item_leaderboard, mUsers);
        mLvLeaderboard.setAdapter(mAdapter);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Settings.BASE_PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        getUsersAndUpdateList();
    }

    public void updateListWithUsers(User[] users) {
        mUsers.addAll(Arrays.asList(users));
        // sorting leaderboard
        Collections.sort(mUsers, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if(o1 == null || o2 == null)
                    return 0;
                return o2.getScore() - o1.getScore();
            }
        });
        mAdapter.notifyDataSetChanged();
    }

    public void getUsersAndUpdateList() {
        IRestAPI service = mRetrofit.create(IRestAPI.class);
        Call<User[]> reportCall = service.getUsers();
        reportCall.enqueue(new Callback<User[]>() {
            @Override
            public void onResponse(Call<User[]> call, Response<User[]> response) {
                if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                    try {
                        Toast.makeText(LeaderboardActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        Log.e(Settings.TAG, "server internal error: " + response.errorBody().string());
                        mPbLoading.setVisibility(View.INVISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                User[] returnedUsers = response.body();
                if(returnedUsers == null) {
                    Toast.makeText(LeaderboardActivity.this, "Data Error", Toast.LENGTH_SHORT).show();
                    Log.e(Settings.TAG, "the users received was null");
                    mPbLoading.setVisibility(View.INVISIBLE);
                    return;
                }

                updateListWithUsers(returnedUsers);
                Log.i(Settings.TAG, "Users were updated at leaderboard successfully (Remote)");
                mPbLoading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<User[]> call, Throwable t) {
                Toast.makeText(LeaderboardActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                Log.e(Settings.TAG, "can't reach server: " + t.toString());
                mPbLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    public class LeaderboardAdapter extends ArrayAdapter<User> {

        private Context _ctx;

        public LeaderboardAdapter (Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            this._ctx = context;
        }

        public LeaderboardAdapter(Context context, int resource, List<User> items) {
            super(context, resource, items);
            this._ctx = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.list_item_leaderboard, parent, false);
            }

            User p = getItem(position);

            if (p != null) {
                TextView tvName = (TextView) v.findViewById(R.id.tvName);
                TextView tvScore = (TextView) v.findViewById(R.id.tvScore);
                ImageView ivPic = (ImageView) v.findViewById(R.id.ivPic);
                TextView tvRank = (TextView) v.findViewById(R.id.tvRank);

                User me = DAL.getInstance().getUser(mPrefs, GameActivity.PREF_TAG_USER);

                if(tvName != null) {
//                    if(me != null && p.equals(me)) {
//                        tvName.setBackgroundResource(R.color.borderVeryVeryLight);
//                    }
                    tvName.setText(p.getName());
                    tvName.setTypeface(Settings.FONT);
                }

                if(tvScore != null) {
//                    if(me != null && p.equals(me)) {
//                        tvScore.setBackgroundResource(R.color.borderVeryVeryLight);
//                    }
                    tvScore.setText(String.valueOf(p.getScore()));
                    tvScore.setTypeface(Settings.FONT);
                }

                if(ivPic!= null) {
//                    if(me != null && p.equals(me)) {
//                        ivPic.setBackgroundResource(R.color.borderVeryVeryLight);
//                    }
//                    ivPic.setImageDrawable(getDrawable(R.drawable.leaderboard));
                    if(p.getPic() != null && !p.getPic().equals(""))
                        Picasso.with(_ctx).load(p.getPic()).into(ivPic);
                }

                if(tvRank != null) {
                    tvRank.setText(String.valueOf(position+1));
                }
            }

            return v;
        }

    }
}
