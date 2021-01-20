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
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.openinstitute.nuru.Database.DatabaseHelper
import com.openinstitute.nuru.R
import com.openinstitute.nuru.adapters.HomePostsListAdapter
import com.openinstitute.nuru.adapters.InitiativesAdapter
import com.openinstitute.nuru.app.AppApiPull
import com.openinstitute.nuru.app.AppFunctions.base64Encode
import com.openinstitute.nuru.app.Globals.KEY_USER_EMAIL
import com.openinstitute.nuru.app.Globals.KEY_USER_SESSION
import com.openinstitute.nuru.models.InitiativesModel
import com.openinstitute.nuru.models.PostsListModel
import com.openinstitute.nuru.utils.CheckInternet
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.sql.Timestamp
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
var context = null
lateinit var requestQueue2: RequestQueue

private lateinit var arrayJsonPosts1: JSONArray
private lateinit var initiativesModelArrayList: ArrayList<InitiativesModel>
private lateinit var initiativesRecyclerView: RecyclerView
private lateinit var homePostsLayoutManager1: LinearLayoutManager
lateinit var initiativesListAdapter: InitiativesAdapter
lateinit var result: String



/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
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
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)


        initiativesRecyclerView = rootView.findViewById(R.id.home_posts_recycler)
        var requestQueue2 = Volley.newRequestQueue(getContext())

        /*if (homePostsRecyclerView != null) {
            homePostsRecyclerView!!.setHasFixedSize(true)
        }*/
        initiativesModelArrayList = ArrayList()
        initiativesListAdapter = InitiativesAdapter(context, initiativesModelArrayList)
        homePostsLayoutManager1 = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        initiativesRecyclerView.setLayoutManager(homePostsLayoutManager1)
        initiativesRecyclerView.setAdapter(initiativesListAdapter)
        try {
            fetchProjects()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        // Inflate the layout for this fragment

        return rootView

    }
    @Throws(UnsupportedEncodingException::class)
    private fun fetchProjects() {
        var requestQueue2 = Volley.newRequestQueue(getContext())

        val url = "https://nuru.sand-box.online/api/projects"
        val preferences = this.activity!!.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        val myToken = preferences.getString("AUTH_TOKEN","")

//        val url = "http://10.0.2.2/oi_nuru_dashboard/api/nuru_test.php?tk=" + Math.random() + "&user_email=alkags@alkags.me"
        val stringRequest: StringRequest = object : StringRequest(
            Method.GET, url, Response.Listener { response ->
            try {
                /* @@ CONVERT RECEIVED STRING TO A JSON-OBJECT */
                val jsonObject_a = JSONObject(response)

                /* @@ GET THE DATA OBJECT HANDLE FROM THE RESPONSE RECEIVED */
                val jsonObject = jsonObject_a.getJSONObject("data")

                /* @@ EVALUATE IF RESULTS WERE FOUND I.E. RESULT OF SUCCESS HANDLE */
//                if (jsonObject.getInt("current_page")) {

                /* @@ GET ARRAY OF RESULTS FROM RESPONSE RECEIVED */
                arrayJsonPosts1 = jsonObject.getJSONArray("data")

                /* @@ CALL FUNCTION TO PUSH ARRAY TO ADAPTER */
                populatePostsList()

//                } else {
//                    Log.e("volliInitiativesERR", "No records found")
//                }
            } catch (e: JSONException) {
                Log.e("volliInitiativesERR", e.toString())
                e.printStackTrace()
            }
        },
            Response.ErrorListener { error -> Log.e("volliInitiativesERR", error.toString()) }
        ) {


            /* @@ this is the part, that adds the header to the request*/

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()

                var authValue = "Bearer " + myToken;
                headers.put("Authorization", authValue);
                headers["Accept"] = "*/*"
                headers["Cache-Control"] = "no-cache"
                headers["Connection"] = "keep-alive"
                return headers
            }
        }
        requestQueue2!!.add(stringRequest)
        //requestQueue!!.cache.clear()
    }

    private fun populatePostsList() {
        initiativesModelArrayList = ArrayList()
        var c: JSONObject? = null
        try {
            val pinsNumber = arrayJsonPosts1!!.length()
            for (i in 0 until pinsNumber) {

                c = arrayJsonPosts1!![i] as JSONObject
                val movie = InitiativesModel(c)

                initiativesModelArrayList!!.add(movie)
                initiativesListAdapter = InitiativesAdapter(context, initiativesModelArrayList)
                initiativesRecyclerView!!.layoutManager = homePostsLayoutManager1
                initiativesRecyclerView!!.adapter = initiativesListAdapter
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }




}