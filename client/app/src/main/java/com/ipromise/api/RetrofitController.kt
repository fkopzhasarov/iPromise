package com.ipromise.api

import android.app.Activity
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonObject
import com.ipromise.activities.LoginActivity
import com.ipromise.activities.MainActivity
import com.ipromise.adapters.PostAdapter
import com.ipromise.adapters.UserAdapter
import com.ipromise.api.models.PostModel
import com.ipromise.api.models.ResponseTokenModel
import com.ipromise.api.models.UserModel
import com.ipromise.prefs.MyPreferences
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitController {
    private val service = RetrofitInstance.create()

    fun register(activity: LoginActivity, view: View, json: JsonObject) {
        service.register(json)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(activity.applicationContext, "Try again later", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Toast.makeText(activity.applicationContext, "Registered successfully", Toast.LENGTH_SHORT).show()
                            activity.register(view)
                        }
                        else Toast.makeText(activity.applicationContext, "User already exists", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    fun login(activity: Activity, preferences: MyPreferences, json: JsonObject) {
        service.login(json)
                .enqueue(object : Callback<ResponseTokenModel> {
                    override fun onFailure(call: Call<ResponseTokenModel>, t: Throwable) {
                        Toast.makeText(activity.applicationContext, "Try again later", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ResponseTokenModel>, response: Response<ResponseTokenModel>) {
                        if (response.isSuccessful) {
                            preferences.setToken(response.body()?.access_token)
                            Toast.makeText(activity.applicationContext, "Logged in successfully", Toast.LENGTH_SHORT).show()
                            activity.startActivity(Intent(activity, MainActivity::class.java))
                            activity.finish()
                        } else Toast.makeText(activity.applicationContext, "Wrong credentials", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    fun follow(activity: Activity, token: String, json: JsonObject, button: Button) {
        service.followUser(token, json)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(activity.applicationContext, "Try again later", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val message = response.body()?.string()
                            message.equals("True")
                            button.text = "Following"
                        }
                    }
                })
    }

    fun unfollow(activity: Activity, token: String, json: JsonObject, button: Button) {
        service.unfollowUser(token, json)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(activity.applicationContext, "Try again later", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val message = response.body()?.string()
                            message.equals("True")
                            button.text = "Follow"
                        }
                    }
                })
    }

    fun isFollowing(activity: Activity, token: String, json: JsonObject, button: Button) {
        service.isFollowingUser(token, json)
                .enqueue(object : Callback<ResponseTokenModel> {
                    override fun onFailure(call: Call<ResponseTokenModel>, t: Throwable) {
                        Toast.makeText(activity.applicationContext, "Try again later", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ResponseTokenModel>, response: Response<ResponseTokenModel>) {
                        if (response.isSuccessful) {
                            val message = response.body()!!.message
                            print(message)
                            button.text = when (message) {
                                "true" -> "Following"
                                "It is you" -> {
                                    button.isEnabled = false
                                    "It is you"
                                }
                                else -> "Follow"
                            }
                        }
                    }
                })
    }

    fun getUserInfo(activity: Activity, token: String, username: TextView?) {
        service.getUserInfo(token)
                .enqueue(object : Callback<UserModel> {
                    override fun onFailure(call: Call<UserModel>, t: Throwable) {
                        Toast.makeText(activity.applicationContext, "Try again later", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                        if (response.isSuccessful) username!!.text = response.body()!!.user_name
                    }
                })
    }

    fun searchUsers(activity: Activity, token: String, json: JsonObject, users: ArrayList<UserModel>, adapter: UserAdapter) {
        service.searchUsers(token, json)
                .enqueue(object : Callback<List<UserModel>> {
                    override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
                        Toast.makeText(activity.applicationContext, "Try again later", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<List<UserModel>>, response: Response<List<UserModel>>) {
                        if (response.isSuccessful)
                            notifyDataSetChanged(users, response.body() as ArrayList<UserModel>, adapter)
                    }
                })
    }

    fun getFollowersList(activity: Activity, token: String, users: ArrayList<UserModel>, adapter: UserAdapter) {
        service.getFollowersList(token)
                .enqueue(object : Callback<List<UserModel>> {
                    override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
                        Toast.makeText(activity.applicationContext, "Try again later", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<List<UserModel>>, response: Response<List<UserModel>>) {
                        if (response.isSuccessful)
                            notifyDataSetChanged(users, response.body() as ArrayList<UserModel>, adapter)
                    }
                })
    }

    fun getFollowedList(activity: Activity, token: String, users: ArrayList<UserModel>, adapter: UserAdapter) {
        service.getFollowedList(token)
                .enqueue(object : Callback<List<UserModel>> {
                    override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
                        Toast.makeText(activity.applicationContext, "Try again later", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<List<UserModel>>, response: Response<List<UserModel>>) {
                        if (response.isSuccessful)
                            notifyDataSetChanged(users, response.body() as ArrayList<UserModel>, adapter)
                    }
                })
    }

    fun addPost(activity: Activity, token: String, json: JsonObject) {
        service.addPost(token, json)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(activity.applicationContext, "Try again later", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            Toast.makeText(activity.applicationContext, "Post created successfully", Toast.LENGTH_SHORT).show()
                            activity.startActivity(Intent(activity, MainActivity::class.java))
                            activity.finish()
                        } else Toast.makeText(activity.applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    fun fetchPosts(activity: Activity, token: String, json: JsonObject, swipeContainer: SwipeRefreshLayout?, posts: ArrayList<PostModel>, adapter: PostAdapter) {
        service.getPosts(token, json)
                .enqueue(object : Callback<List<PostModel>> {
                    override fun onFailure(call: Call<List<PostModel>>, t: Throwable) {
                        Toast.makeText(activity.applicationContext, "Try again later", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<List<PostModel>>, response: Response<List<PostModel>>) {
                        if (response.isSuccessful)
                            notifyDataSetChanged(posts, response.body() as ArrayList<PostModel>, adapter)
                        swipeContainer?.isRefreshing = false
                    }
                })
    }

    fun <T, U : RecyclerView.ViewHolder, V : RecyclerView.Adapter<U>> notifyDataSetChanged(list: ArrayList<T>, data: ArrayList<T>, adapter: V) {
        list.clear()
        list.addAll(data)
        adapter.notifyDataSetChanged()
    }
}