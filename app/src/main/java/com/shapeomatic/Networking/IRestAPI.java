package com.shapeomatic.Networking;

import com.shapeomatic.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Alex on 9/4/2017.
 */

public interface IRestAPI {
    /**
     * get the user details from server.
     * and from the specified unit id.
     * returns status 404 NOT FOUND if users does not exist.
     */
    @GET("user/{user-id}/")
    Call<User> getUserById(@Path("user-id") long userId);

    /**
     * get all users from server.
     */
    @GET("user/")
    Call<User[]> getUsers();

    /**
     * creates a new user.
     * returns error if user already exists.
     * returns nothing
     */
    @POST("user/")
    Call<Void> createUser(@Body User user);

    /**
     * update existing user.
     * returns error if user doesn't exists.
     * returns nothing
     */
    @PUT("user/{user-id}/")
    Call<Void> updateUser(@Path("user-id") long userId ,@Body User user);
}
