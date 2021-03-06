package com.ipromise.fragments

import android.app.Fragment
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.LinearLayout
import com.google.gson.JsonObject
import com.ipromise.R
import com.ipromise.adapters.PostAdapter
import com.ipromise.api.RetrofitController
import com.ipromise.api.models.PostModel
import com.ipromise.prefs.MyPreferences

class FeedFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val posts = ArrayList<PostModel>()
        val adapter: PostAdapter
        val view = inflater.inflate(R.layout.feed_fragment, container, false)
        val recyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        val swipeContainer = view.findViewById(R.id.swipeContainer) as SwipeRefreshLayout
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        adapter = PostAdapter(posts)
        recyclerView.adapter = adapter
        RetrofitController().fetchPosts(activity, MyPreferences(activity!!.applicationContext).getToken(), JsonObject(), swipeContainer, posts, adapter)
        swipeContainer.setOnRefreshListener({
            RetrofitController().fetchPosts(activity, MyPreferences(activity!!.applicationContext).getToken(), JsonObject(), swipeContainer, posts, adapter)
        })
        swipeContainer.setColorSchemeResources(android.R.color.black)
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_feed, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}