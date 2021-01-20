package com.openinstitute.nuru.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.openinstitute.nuru.R
import com.openinstitute.nuru.adapters.InitiativesAdapter
import com.openinstitute.nuru.models.InitiativesModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.util.*

class InitiativesFragment : Fragment() {
    var context = null
    private lateinit var arrayJsonPosts: JSONArray
    var requestQueue: RequestQueue? = null
    private lateinit var initiativesModelArrayList: ArrayList<InitiativesModel>
    private lateinit var initiativesRecyclerView: RecyclerView
    private lateinit var homePostsLayoutManager: LinearLayoutManager
    lateinit var initiativesListAdapter: InitiativesAdapter
    lateinit var result: String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_initiatives, container, false)

        //context = context

        requestQueue = Volley.newRequestQueue(getContext())

       initiativesRecyclerView = rootView.findViewById(R.id.rvInitiatives)
        /*if (homePostsRecyclerView != null) {
            homePostsRecyclerView!!.setHasFixedSize(true)
        }*/
        initiativesModelArrayList = ArrayList()
        initiativesListAdapter = InitiativesAdapter(context, initiativesModelArrayList)
        homePostsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        initiativesRecyclerView.setLayoutManager(homePostsLayoutManager)
        initiativesRecyclerView.setAdapter(initiativesListAdapter)
        try {
            fetchProjects()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return rootView
    }

    @Throws(UnsupportedEncodingException::class)
    private fun fetchProjects() {

        val url = "https://nuru.sand-box.online/api/projects"
        val preferences = this.activity!!.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        val myToken = preferences.getString("AUTH_TOKEN","")

//        val url = "http://10.0.2.2/oi_nuru_dashboard/api/nuru_test.php?tk=" + Math.random() + "&user_email=alkags@alkags.me"
        val stringRequest: StringRequest = object : StringRequest(Method.GET, url, Response.Listener { response ->
            try {
                /* @@ CONVERT RECEIVED STRING TO A JSON-OBJECT */
                val jsonObject_a = JSONObject(response)

                /* @@ GET THE DATA OBJECT HANDLE FROM THE RESPONSE RECEIVED */
                val jsonObject = jsonObject_a.getJSONObject("data")

                /* @@ EVALUATE IF RESULTS WERE FOUND I.E. RESULT OF SUCCESS HANDLE */
//                if (jsonObject.getInt("current_page")) {

                /* @@ GET ARRAY OF RESULTS FROM RESPONSE RECEIVED */
                arrayJsonPosts = jsonObject.getJSONArray("data")

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
        requestQueue!!.add(stringRequest)
        //requestQueue!!.cache.clear()
    }

    private fun populatePostsList() {
        initiativesModelArrayList = ArrayList()
        var c: JSONObject? = null
        try {
            val pinsNumber = arrayJsonPosts!!.length()
            for (i in 0 until pinsNumber) {

                c = arrayJsonPosts!![i] as JSONObject
                val movie = InitiativesModel(c)

                initiativesModelArrayList!!.add(movie)
                initiativesListAdapter = InitiativesAdapter(context, initiativesModelArrayList)
                initiativesRecyclerView!!.layoutManager = homePostsLayoutManager
               initiativesRecyclerView!!.adapter = initiativesListAdapter
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }



    companion object {
        fun newInstance(): InitiativesFragment {
            return InitiativesFragment()
        }
    }

}