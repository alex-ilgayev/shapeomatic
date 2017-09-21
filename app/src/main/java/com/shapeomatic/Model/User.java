package com.shapeomatic.Model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alex on 7/8/2016.
 */
@XStreamAlias("User")
public class User implements Serializable{

    @SerializedName("facebook_id")
    @Expose
    private long facebookId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("pic")
    @Expose
    private String pic;

    @SerializedName("high_score")
    @Expose
    private int score;

    private ArrayList<User> friends;

    // indicating if the user is created at the server.
    // some flag for making post request for the server.
    private boolean isRemotelyCreated;

    // indicating if the user is received info from facebook remote.
    private boolean isFacebookSynced;

    public User(long facebookId, String name, String pic, int score, ArrayList<User> friends,
                boolean isCreated, boolean isFacebookSynced) {
        this.facebookId = facebookId;
        this.name = name;
//        this.pic = pic;
        this.pic = "asdfsa";
        this.score = score;
        this.friends = friends;
        this.isRemotelyCreated = isCreated;
        this.isFacebookSynced = isFacebookSynced;
    }

    public User() {
        this(0, "Annonymous", "", 0, null, false, false);
    }

    public long getFacebookId() {
        return facebookId;
    }

    public String getName() {
        return name;
    }

    public String getPic() {
        return pic;
    }

    public int getScore() {
        return score;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean getIsCreated() {
        return isRemotelyCreated;
    }

    public void setUserCreated() {
        isRemotelyCreated = true;
    }

    public void setUserNotCreated() {
        isRemotelyCreated = false;
    }

    public boolean getIsFacebookSynced() {
        return isFacebookSynced;
    }

    public void setIsFacebookSynced(boolean isFacebookSynced) {
        this.isFacebookSynced = isFacebookSynced;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return facebookId == user.facebookId;

    }

    @Override
    public int hashCode() {
        return (int) (facebookId ^ (facebookId >>> 32));
    }
}
