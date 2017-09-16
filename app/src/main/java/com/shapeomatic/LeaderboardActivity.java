package com.shapeomatic;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shapeomatic.Controller.DAL;
import com.shapeomatic.Controller.Settings;
import com.shapeomatic.Model.User;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    SharedPreferences mPrefs;

    TextView mTvLeaderboardTitle;
    ListView mLvLeaderboard;
    LeaderboardAdapter mAdapter;
    List<User> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        mPrefs = getSharedPreferences(GameActivity.PREF_TAG, Context.MODE_PRIVATE);

        mUsers = new LinkedList<>();

        User me = DAL.getInstance().getUser(mPrefs, GameActivity.PREF_TAG_USER);

//        mUsers.add(new User(Long.parseLong("107154476387602"), "Alex Ilgayev", "sadfas", 111, null));
//        mUsers.add(new User(111, "adasdas", "sadfas", 100, null));

        mUsers.add(me);
        if(me.getFriends() != null) {
            for(User friend: me.getFriends()) {
                mUsers.add(friend);
            }
        }

        mTvLeaderboardTitle = (TextView) findViewById(R.id.tvLeaderboardTitle);
        mTvLeaderboardTitle.setTypeface(Settings.FONT);

        mLvLeaderboard = (ListView) findViewById(R.id.lvLeaderboard);
        mAdapter = new LeaderboardAdapter(this, R.layout.list_item_leaderboard, mUsers);
        mLvLeaderboard.setAdapter(mAdapter);
    }

    public class LeaderboardAdapter extends ArrayAdapter<User> {

        public LeaderboardAdapter (Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public LeaderboardAdapter(Context context, int resource, List<User> items) {
            super(context, resource, items);
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
                    if(me != null && p.equals(me)) {
                        tvName.setBackgroundResource(R.color.borderVeryVeryLight);
                    }
                    tvName.setText(p.getName());
                    tvName.setTypeface(Settings.FONT);
                }

                if(tvScore != null) {
                    if(me != null && p.equals(me)) {
                        tvScore.setBackgroundResource(R.color.borderVeryVeryLight);
                    }
                    tvScore.setText(String.valueOf(p.getScore()));
                    tvScore.setTypeface(Settings.FONT);
                }

                if(ivPic!= null) {
                    if(me != null && p.equals(me)) {
                        ivPic.setBackgroundResource(R.color.borderVeryVeryLight);
                    }
                    ivPic.setImageDrawable(getDrawable(R.drawable.leaderboard));
                }

                if(tvRank != null) {
                    tvRank.setText(String.valueOf(position+1));
                }
            }

            return v;
        }

    }
}
