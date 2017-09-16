package com.shapeomatic.Controller;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.shapeomatic.Model.User;

/**
 * Created by Alex on 7/9/2016.
 */
public class DAL {
    private static DAL _ins = null;

    private DAL() {

    }

    public static DAL getInstance() {
        if(_ins == null)
            _ins = new DAL();
        return _ins;
    }

    public void putUser(SharedPreferences prefs, String tag , User user) {
        SharedPreferences.Editor edit = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        edit.putString(tag, json);
        edit.commit();
    }

    public User getUser(SharedPreferences prefs, String tag) {
        Gson gson = new Gson();
        String json = prefs.getString(tag, "");
        User obj = gson.fromJson(json, User.class);
        return obj;
    }
}
