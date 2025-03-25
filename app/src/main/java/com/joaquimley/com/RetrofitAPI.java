package com.joaquimley.com;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @POST("api/message")

        //on below line we are creating a method to post our data.
    Call<Void> sendMessage(@Body Massage dataModal);

    @GET("api/user")
    Call<List<UserModel>> getUsers();

    @POST("api/register")
    Call<Void> addUser(@Body UserModel userModel);


}
