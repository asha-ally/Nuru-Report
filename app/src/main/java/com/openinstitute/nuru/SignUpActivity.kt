package com.openinstitute.nuru

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.hbb20.CountryCodePicker
import com.openinstitute.nuru.AppFunctions.func_showToast
import com.openinstitute.nuru.Database.DatabaseHelper
import com.openinstitute.nuru.app.WebAsyncPost
import com.openinstitute.nuru.rest.ApiClient
import com.openinstitute.nuru.rest.ApiInterface
import com.openinstitute.nuru.rest.RegistrationResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    lateinit var signupButton: Button
    lateinit var etName :EditText
    lateinit var etProfile :Spinner
    lateinit var etNumber:EditText
    lateinit var etEmail :EditText
    lateinit var etPassword :EditText
    lateinit var _loginLink :TextView
    lateinit var etPasswordText2:EditText
    lateinit var countryCode :CountryCodePicker
    lateinit var  checkBox :CheckBox
    private var asyncForm: WebAsyncPost? = null

    var context = this@SignUpActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        signupButton = findViewById(R.id.btn_signup1)
        etName = findViewById(R.id.input_name)
        etProfile = findViewById(R.id.profile_array)
        etNumber = findViewById<EditText>(R.id.input_number)
         etEmail = findViewById<EditText>(R.id.input_email1)
         etPassword = findViewById<EditText>(R.id.input_password1)
         _loginLink = findViewById<TextView>(R.id.link_login)
         etPasswordText2 = findViewById<EditText>(R.id.input_password2)
         countryCode = findViewById<CountryCodePicker>(R.id.ccp)
          checkBox = findViewById<CheckBox>(R.id.checkBox)



        signupButton.setOnClickListener {
            if (checkBox.isChecked) {
                signup()
            } else func_showToast(this.applicationContext, "Please accept terms and conditions")
        }





    }



    //signup logic
    fun signup() {
        //Log.d(TAG, "Signup");
        if (!validate()) {
            onSignupFailed()
            return
        }
//        signupButton.setEnabled(false)
        val progressDialog = ProgressDialog(this@SignUpActivity)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()


        val name: String = etName.text.toString()
        val profile: String = etProfile.selectedItem.toString()
        val phone: String = etNumber.text.toString()
        val email: String = etEmail.text.toString()
        val password: String =etPassword.text.toString()
        val password_confirmation: String =etPasswordText2.text.toString()
        val ccp: String = countryCode.selectedCountryCode
        val user = JsonObject()
        try {
            user.addProperty("name", name)
            user.addProperty("profile", profile)
            user.addProperty("phone", "$ccp-$phone")
            user.addProperty("email", email)
            user.addProperty("password", password)
            user.addProperty("password_confirmation", password_confirmation)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        registerUser(user)



        val databaseHelper = DatabaseHelper(context)
//        databaseHelper.addAccountNew(json)




        Handler().postDelayed(
            { // On complete call either onSignupSuccess or onSignupFailed
                // depending on success
                onSignupSuccess()
                // onSignupFailed();
                progressDialog.dismiss()
            }, 3000
        )
    }


    fun onSignupSuccess() {
        signupButton.isEnabled = true
        setResult(RESULT_OK, null)
        //finish();
        display_MainActivity()
    }

    fun onSignupFailed() {
        Toast.makeText(
            baseContext,
            "Please check if you have put all the details",
            Toast.LENGTH_LONG
        ).show()
        signupButton.isEnabled = true
    }

    fun validate(): Boolean {
        var valid = true
        val name: String = etName.text.toString()
        val number: String = etNumber.text.toString()
        val email: String = etEmail.text.toString()
        val password: String = etPassword.text.toString()
        val confirmpassword: String = etPasswordText2.text.toString()

        if (name.isEmpty() || name.length < 3 && name.contains(" ")) {
            etName.error = "please input 2 names"
            valid = false
        } else {
            etName.error = null
        }

        if (number.isEmpty() || number.length < 9) {
            etNumber.error = "at least 9 characters"
            valid = false
        } else {
            etNumber.error = null
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "enter a valid email address"
            valid = false
        } else {
            etEmail.error = null
        }
        if (password.isEmpty() || password.length < 4) { /*|| password.length() > 10*/
            etPassword.error = "at least 4 characters"
            valid = false
        } else {
            etPassword.error = null
        }
        if (confirmpassword.isEmpty() || confirmpassword != password) { /*password.length() < 4 || password.length() > 10*/
            etPasswordText2.error = "Passwords don't match try again "
            valid = false
        } else {
            etPasswordText2.error = null
        }
        return valid
    }


    fun display_MainActivity() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }



    fun userAsyncResponse(response: String) {
        /*Log.d("userString", response + " -- ");*/
        if (response == "Success") {
//            func_showAlerts(context, "Saved to server.", "");
        } else {
//            func_showAlerts(context, "Save to server FAILED!", "warning");
        }
    }

    //register user using Retrofit

    fun registerUser(user: JsonObject) {
        val apiInterface: ApiInterface = ApiClient.createRetrofit().create(ApiInterface::class.java)
        val registrationCall: Call<RegistrationResponse> = apiInterface.registerUser(user)
        registrationCall.enqueue(object : Callback<RegistrationResponse?> {
            override fun onResponse(
                call: Call<RegistrationResponse?>,
                response: Response<RegistrationResponse?>
            ) {
                if (response.isSuccessful) {
                    val registrationResponse: RegistrationResponse? = response.body()
                    val msharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                        baseContext
                    )
                    val editor = msharedPreferences.edit()
                    if (registrationResponse != null) {
                        editor.putInt("USER_ID", registrationResponse.id)
                    }
                    if (registrationResponse != null) {
                        editor.putString("AUTH_TOKEN", registrationResponse.token)
                    }
                    editor.commit()
                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<RegistrationResponse?>, t: Throwable) {
                Log.e("failure", t.message)
            }
        })
    }


}



