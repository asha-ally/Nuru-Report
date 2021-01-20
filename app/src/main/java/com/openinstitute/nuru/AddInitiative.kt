package com.openinstitute.nuru

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.openinstitute.nuru.AppFunctions.func_showToast
import com.openinstitute.nuru.views.context
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.*

class AddInitiative : AppCompatActivity() {

    lateinit var start_date: TextView
    lateinit var end_date: TextView
    lateinit var ettitle:EditText
    lateinit var etdetails:EditText
    lateinit var requestQueue: RequestQueue
    lateinit var spinner: Spinner
    lateinit var title1:String
    lateinit var details:String
    lateinit var button:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_initiative)
        title = "Add Initiative"
        spinner = findViewById<Spinner>(R.id.category_array)
        spinner.onItemSelectedListener

        button= findViewById(R.id.btnSaveI )


        start_date = findViewById<TextView>(R.id.start_date)
        end_date = findViewById<TextView>(R.id.end_date)
        ettitle = findViewById(R.id.etTitle)
        etdetails = findViewById(R.id.etDescription)
        title1 = ettitle.text.toString()
        details = etdetails.text.toString()
        var context = this




        start_date.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            start_date.text = sdf.format(cal.time)

        }

        start_date.setOnClickListener {
            DatePickerDialog(this@AddInitiative, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        end_date.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())


        val dateSetListener1 = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            end_date.text = sdf.format(cal.time)

        }

        end_date.setOnClickListener {
            DatePickerDialog(this@AddInitiative, dateSetListener1,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        val initiative = JSONObject()

        try {
            initiative.put("title", title1)
            initiative.put("narration",details)
            initiative.put("start_date",start_date.text)
            initiative.put("end_date",end_date.text)

        }catch (e: JSONException) {
            e.printStackTrace()
        }
        button.setOnClickListener(
                View.OnClickListener {
                    postInitiative()
                    val alertDialogBuilder = AlertDialog.Builder(context)
                    alertDialogBuilder.setTitle("Choose your preffered method for data collection")
                    alertDialogBuilder.setMessage("Signing out. Confirm!").setCancelable(false).setPositiveButton(
                            R.string.lbl_yes
                    ) { dialog, id ->
                        dialog.cancel()

                    }.setNegativeButton(R.string.lbl_no) { dialog, id -> dialog.cancel() }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()


                    ettitle.setText("")
                    etdetails.setText("")

                }
        )





    }


    @Throws(UnsupportedEncodingException::class)
    private fun postInitiative() {
        requestQueue = Volley.newRequestQueue(this)


        val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        val myToken = preferences.getString("AUTH_TOKEN","")
        val url = " https://nuru.sand-box.online/api/projects"


        val stringRequest: StringRequest = object : StringRequest(Method.POST, url, Response.Listener { response ->
            val initiative = JSONObject()

            try {
                initiative.put("title", title1)
                initiative.put("narration",details)
                initiative.put("start_date",start_date.text)
                initiative.put("end_date",end_date.text)
                initiative.put("tags","")
                initiative.put("categories","")

            } catch (e: JSONException) {
                Log.e("volliInitiativesERR", e.toString())
                func_showToast(context,"Server Error")
                e.printStackTrace()
            }
        },
                Response.ErrorListener {
                        error -> Log.e("volliInitiativesERR", error.toString())
                }
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
    }





}




