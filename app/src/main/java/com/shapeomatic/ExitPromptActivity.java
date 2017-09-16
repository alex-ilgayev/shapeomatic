package com.shapeomatic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class ExitPromptActivity extends Activity {

    public static final String TAG_MAIN_MENU = "MainMenu";

    private int mWidth;
    private int mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_prompt);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        View v = (View) findViewById(R.id.btnMainMenu);
        Object isMenuOn = getIntent().getExtras().get(TAG_MAIN_MENU);
        if(isMenuOn == null || (Boolean)isMenuOn == true) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    protected void onClickReturn(View v) {
        finish();
    }

    protected void onClickExit(View v) {
        finish();
        System.exit(0);
    }

    protected void onClickMainMenu(View v) {
        Intent i = new Intent(this, GameActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}