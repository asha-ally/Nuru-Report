package com.openinstitute.nuru

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.JsonObject
import com.openinstitute.nuru.views.context
//import com.openinstitute.nuru.views.requestQueue
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class LoginActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private lateinit var firebaseAuth: FirebaseAuth
    val TAG:String = "LoginActivity"

    private val RC_SIGN_IN = 7

    lateinit var sharedPref: SharedPreferences
    lateinit var loginbutton: Button
    lateinit var requestQueue1: RequestQueue





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        sharedPref = getSharedPreferences("loginPrefs", MODE_PRIVATE)


        val login = findViewById<Button>(R.id.btn_login)

        firebaseAuth = FirebaseAuth.getInstance()

        configureGoogleSignIn()


        val google_button = findViewById<SignInButton>(R.id.sign_in_button)
        google_button.setOnClickListener {
            signIn()}


        val signuplink = findViewById<TextView>(R.id.link_signup)
        signuplink.setOnClickListener{
            register()}

        val user_email = sharedPref.getString("user_email", null)
        val   user_logged = sharedPref.getBoolean("user_logged", false)


        if (user_email !=null){
                displayMainActivity()
                finish()
            }
         loginbutton = findViewById(R.id.btn_login)

        loginbutton.setOnClickListener {
            login()
        }


    }


    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {

            val userId: String = user.uid
            val userEmail: String = user.email!!
            sharedPref = getPreferences(MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("firebasekey", userId)
            editor.commit()
            val i: Intent = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }



    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun register(){

        val register: Intent = Intent(this, SignUpActivity::class.java)
        startActivity(register)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Please check your internet", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val i: Intent = Intent(this, MainActivity::class.java)
                startActivity(i)
            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }



    fun login(){

        if (!validate()) {
            onLoginFailed()
            return
        }


        var _emailText: EditText= findViewById(R.id.input_email)
        var _passwordText: EditText= findViewById(R.id.input_password)
        var email: String= _emailText.text.toString()
        var password: String= _passwordText.text.toString()


        loginbutton.isEnabled = false
        val progressDialog = ProgressDialog(this@LoginActivity)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Authenticating...")
        progressDialog.show()

        val user = JsonObject()
        try {
            val editor = sharedPref.edit()

            editor.putString("email", email)
            editor.putString("id", email)
            editor.commit()

            user.addProperty("email", email)
            user.addProperty("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        loginUser(email,password)
        displayMainActivity()

    }


    fun validate(): Boolean {

        var _emailText: EditText= findViewById(R.id.input_email)
        var _passwordText: EditText= findViewById(R.id.input_password)
        var email: String= _emailText.text.toString()
        var password: String= _passwordText.text.toString()


        var valid = true

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.error = "enter a valid email address"
            valid = false
        } else {
            _emailText.error = null
        }
        if (password.isEmpty() || password.length < 4) { /*|| password.length() > 10*/
            _passwordText.error = "at least 4 characters"
            valid = false
        } else {
            _passwordText.error = null
        }

        return valid
    }
//    fun loginUser(user: JsonObject) {
//        Log.d("user",user.toString())
//
//        val apiInterface: ApiInterface = ApiClient.createRetrofit().create(ApiInterface::class.java)
//        val loginCall: retrofit2.Call<LoginRequest> = apiInterface.loginUser(user)
//        loginCall.enqueue(object : Callback<LoginRequest?>{
//
//            override fun onResponse(
//                call: retrofit2.Call<LoginRequest?>,
//                response: Response<LoginRequest?>
//            ) {
//
//                if (response.isSuccessful) {
//                    val loginRequest: LoginRequest? = response.body()
//                    Log.d("response",response.toString())
//
//
////                    val msharedPreferences = PreferenceManager.getDefaultSharedPreferences(
////                        baseContext
////                    )
//                    val editor = sharedPref.edit()
//                    if (loginRequest != null) {
//                        editor.putInt("USER_ID", loginRequest.code)
//                    }
//                    if (loginRequest != null) {
//                        editor.putString("AUTH_TOKEN", loginRequest.token)
//                    }
//                    editor.commit()
//
//
//                    val intent = Intent(baseContext, MainActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//
//            override fun onFailure(call: retrofit2.Call<LoginRequest?>, t: Throwable) {
//                Log.e("failure", t.message)
//            }
//
//
//        })
//    }

    @Throws(UnsupportedEncodingException::class)
    private fun loginUser(email:String,password:String) {

        requestQueue1 = Volley.newRequestQueue(this)

//        val email = "asha@mail.com"
//        val pass = "123456"
        val url = "https://nuru.sand-box.online/api/token?email=$email&password=$password"
        val stringRequest = StringRequest(Request.Method.POST, url, object : Response.Listener<String?> {
            override
            fun onResponse(response: String?) {
                try {
                    val jsonObject = JSONObject(response)
                    Log.d("volliRESP", jsonObject.toString())
                    Log.d("volliDATA", jsonObject.getString("data"))
                    val rawData = JSONObject(jsonObject.getString("data"))
                    Log.d("volliTOKEN", rawData.getString("token"))

                    val editor = sharedPref.edit()
                    editor.putString("AUTH_TOKEN",rawData.getString("token"))
                    editor.commit()
                    //Toast.makeText(context,jsonObject.getString("data"),Toast.LENGTH_LONG).show();
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(context, "err $e", Toast.LENGTH_LONG).show()
                }
            }
        },
                object : Response.ErrorListener {
                    override
                    fun onErrorResponse(error: VolleyError) {
                        Toast.makeText(context, "err " + error.toString(), Toast.LENGTH_LONG).show()
                        Log.d("Vollierror",error.toString())
                    }
                }
        )
        requestQueue1.add(stringRequest)
    }

    fun onLoginSuccess() {
        loginbutton.isEnabled = true
        setResult(RESULT_OK, null)
        //finish();
        displayMainActivity()
    }

    fun onLoginFailed() {
        Toast.makeText(
                baseContext,
                "Please check if you have put all the details",
                Toast.LENGTH_LONG
        ).show()
        loginbutton.isEnabled = true
    }

    fun displayMainActivity(){
        val mainIntent: Intent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }








}