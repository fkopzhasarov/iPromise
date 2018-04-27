package com.ipromise.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.ipromise.R
import com.ipromise.adapters.PostAdapter
import com.ipromise.adapters.UserAdapter
import com.ipromise.api.RetrofitController
import com.ipromise.api.models.PostModel
import com.ipromise.api.models.UserModel
import com.ipromise.prefs.MyPreferences
import com.ipromise.utils.JSONBuilder


class ProfileFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var usernameField: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val token = MyPreferences(activity!!.applicationContext).getToken()
        val view = inflater.inflate(R.layout.tab2_fragment, container, false)
        usernameField = view.findViewById<TextView>(R.id.profile_username)
        recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        RetrofitController().getUserInfo(token, usernameField)
        val profile_posts_button = view.findViewById<Button>(R.id.profile_posts)
        val profile_followers_button = view.findViewById<Button>(R.id.profile_followers)
        val profile_followed_button = view.findViewById<Button>(R.id.profile_followed)
        profile_posts_button.setOnClickListener({ getUserPosts(view) })
        profile_followers_button.setOnClickListener({ getFollowersList(view) })
        profile_followed_button.setOnClickListener({ getFollowedList(view) })
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun getUserPosts(view: View) {
        val posts = ArrayList<PostModel>()
        val adapter = PostAdapter(posts)
        recyclerView!!.adapter = adapter
        RetrofitController().fetchPosts(MyPreferences(activity!!.applicationContext).getToken(),
                JSONBuilder().append("user_name", usernameField!!.text.toString()).build(), posts, adapter)
    }

    fun getFollowersList(view: View) {
        val users = ArrayList<UserModel>()
        val adapter = UserAdapter(users, activity!!.applicationContext)
        recyclerView!!.adapter = adapter
        RetrofitController().getFollowersList(MyPreferences(activity!!.applicationContext).getToken(), users, adapter)
    }

    fun getFollowedList(view: View) {
        val users = ArrayList<UserModel>()
        val adapter = UserAdapter(users, activity!!.applicationContext)
        recyclerView!!.adapter = adapter
        RetrofitController().getFollowedList(MyPreferences(activity!!.applicationContext).getToken(), users, adapter)
    }
}