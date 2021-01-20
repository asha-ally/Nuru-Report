package com.openinstitute.nuru.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.Volley
import com.openinstitute.nuru.Database.DatabaseHelper
import com.openinstitute.nuru.R
import com.openinstitute.nuru.adapters.HomePostsListAdapter
import com.openinstitute.nuru.app.AppApiPull
import com.openinstitute.nuru.app.AppFunctions.base64Encode
import com.openinstitute.nuru.app.AppFunctions.func_showToast
import com.openinstitute.nuru.app.Globals.KEY_USER_EMAIL
import com.openinstitute.nuru.app.Globals.KEY_USER_SESSION
import com.openinstitute.nuru.models.PostsListModel
import com.openinstitute.nuru.utils.CheckInternet
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.sql.Timestamp


import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

lateinit var db: DatabaseHelper

var preferences: SharedPreferences? = null
lateinit var user_name: String
lateinit var user_email:String
lateinit var session_id:String
lateinit var arrayJsonPosts: JSONArray
//lateinit var requestQueue: RequestQueue
lateinit var postsListModelArrayList: ArrayList<PostsListModel>

lateinit var homePostsRecyclerView: RecyclerView
lateinit var homePostsLayoutManager: LinearLayoutManager
lateinit var appPinsListAdapter: HomePostsListAdapter


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)


        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView =inflater.inflate(R.layout.fragment_profile, container, false)

//        context = context
        db = DatabaseHelper(context)



        CheckInternet(context).checkConnection()
//        requestQueue = Volley.newRequestQueue(context)
//
        homePostsRecyclerView = rootView.findViewById(R.id.rvPosts)

        if (homePostsRecyclerView != null) {
            homePostsRecyclerView.setHasFixedSize(true)
        }
        //using staggered grid pattern in recyclerview
        //mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        //using staggered grid pattern in recyclerview
        //mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        postsListModelArrayList = ArrayList()
        appPinsListAdapter = HomePostsListAdapter(context, postsListModelArrayList)

        homePostsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        homePostsRecyclerView.layoutManager = homePostsLayoutManager
        homePostsRecyclerView.adapter = appPinsListAdapter

//        get_postsList()

        return rootView


        // Inflate the layout for this fragment
    }

//    private fun get_postsList() {
//
//
//        val preferences = this.activity!!.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
//        user_email= preferences.getString("user_email", "")!!
//        user_name = preferences.getString("user_name", "")!!
//
//
//        requestQueue.start()
//       lateinit var order_data: String
//        var jsonString: String
//        val action_time = Timestamp(System.currentTimeMillis())
//        val action_time_id = action_time.time.toString()
//        session_id = action_time_id
//        var post_b: JSONObject? = null
//        try {
//            post_b = JSONObject()
//            post_b.put(KEY_USER_EMAIL, user_email)
//            post_b.put(KEY_USER_SESSION, session_id)
//            jsonString = post_b.toString()
//            order_data = base64Encode(jsonString)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        val appPostList = AppApiPull(order_data, "homeposts", Response.Listener<String> { response -> //Log.d("postsResponse", response);
//            try {
//                val jsonObject_a = JSONObject(response)
//                val jsonObject = jsonObject_a.getJSONObject("response")
//                if (jsonObject.getBoolean("success")) {
//                    arrayJsonPosts = jsonObject.getJSONArray("postslist")
//                    Log.d("arrayJsonPosts", arrayJsonPosts.toString())
//                    populatePostsList()
//                } else {
//                    func_showToast(context, "Please create a new post")
//                }
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//        }){
//            error ->  func_showToast(context,"Server Error")
//        }
//
//        requestQueue.add<Any>(appPostList)
//        requestQueue.cache.clear()
//    }
//
//
//    private fun populatePostsList() {
//        postsListModelArrayList = ArrayList()
//        var c: JSONObject? = null
//
//        try {
//            val pinsNumber = arrayJsonPosts.length()
//            for (i in 0 until pinsNumber) {
//                c = arrayJsonPosts[i] as JSONObject
//                val movie = PostsListModel(c)
//                postsListModelArrayList.add(movie)
//                appPinsListAdapter = HomePostsListAdapter(context, postsListModelArrayList)
//                homePostsRecyclerView.layoutManager = homePostsLayoutManager
//                homePostsRecyclerView.adapter = appPinsListAdapter
//            }
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}