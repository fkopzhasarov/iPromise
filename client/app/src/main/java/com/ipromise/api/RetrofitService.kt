package com.ipromise.api

import com.google.gson.JsonObject
import com.ipromise.api.models.PostModel
import com.ipromise.api.models.ResponseTokenModel
import com.ipromise.api.models.UserModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RetrofitService {
    @POST("/register")
    fun register(@Body body: JsonObject): Call<ResponseBody>

    @POST("/login")
    fun login(@Body body: JsonObject): Call<ResponseTokenModel>

    @GET("/user")
    fun getUserInfo(@Header("Authorization") authorization: String): Call<UserModel>

    @POST("/followers")
    fun getFollowersList(@Header("Authorization") authorization: String): Call<List<UserModel>>

    @POST("/followed")
    fun getFollowedList(@Header("Authorization") authorization: String): Call<List<UserModel>>

    @POST("/follow")
    fun followUser(@Header("Authorization") authorization: String, @Body body: JsonObject): Call<ResponseBody>

    @POST("/unfollow")
    fun unfollowUser(@Header("Authorization") authorization: String, @Body body: JsonObject): Call<ResponseBody>

    @POST("/is_following")
    fun isFollowingUser(@Header("Authorization") authorization: String, @Body body: JsonObject): Call<ResponseTokenModel>

    @POST("/add_post")
    fun addPost(@Header("Authorization") authorization: String, @Body body: JsonObject): Call<ResponseBody>

    @POST("/remove_post")
    fun removePost(@Header("Authorization") authorization: String, @Body body: JsonObject): Call<ResponseBody>

    @POST("/get_posts")
    fun getPosts(@Header("Authorization") authorization: String, @Body body: JsonObject): Call<List<PostModel>>

    @POST("/search_user")
    fun searchUsers(@Header("Authorization") authorization: String, @Body body: JsonObject): Call<List<UserModel>>
}