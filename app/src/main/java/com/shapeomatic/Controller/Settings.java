package com.shapeomatic.Controller;

import android.graphics.Typeface;

/**
 * Created by Alex on 5/4/2016.
 */
public class Settings {

    public static final String TAG = "Shapeomatic";

    public static final int TIMER_INTERVAL_MILLIS_DEFAULT = 800;
    public static int TIMER_INTERVAL_MILLIS;
    public static final int LEVEL_INTERVAL_MILLIES = 4000;
    public static final int MAX_LEVEL = 43;
    public static final int VIBRATION_DURATION_MILLIS = 200;
    public static final int RADIUS = 80;
    public static final int SCALE_STEPS = 5;
    public static final int DESCALE_STEPS = 10;
    public static final int MAX_HEARTS = 4;
    public static final int MAX_LIVES = 20;
    public static final int LEVEL_CROSSFADE_MILLIES = 1000;
    public static final int PICKER_ANIMATION_SCALE_STEP = 10;
    public static final int SCORE_STEPS = 1;

    //Networking
    //public static final String BASE_PATH = "http://localhost:35168/";
//    public static final String BASE_PATH  = "http://10.0.2.2:35168/api/";
    //public static final String BASE_PATH  = "http://10.0.2.2/api/";
//    public static final String BASE_PATH  = "http://192.168.42.20/api/";
//    public static final String BASE_PATH = "http://10.0.2.2:8000/";
    public static final String BASE_PATH = "https://shapeomatic.herokuapp.com";

//    public static final String URL_USER_PATH = BASE_PATH + "user/";

    public static final int CONNECT_TIMEOUT = 5000;
    public static final int READ_TIMEOUT = 5000;
    public static final int MAX_RETRIES = 3;

    public static Typeface FONT = null;
}
