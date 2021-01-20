package com.openinstitute.nuru

import android.app.Dialog
import android.app.SearchManager
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.openinstitute.nuru.adapters.InitiativesAdapter
import com.openinstitute.nuru.app.Globals.*
import com.openinstitute.nuru.views.*
import org.json.JSONArray


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawer_layout: DrawerLayout

    private lateinit var navigationView:NavigationView
    lateinit var toolbar :androidx.appcompat.widget.Toolbar
    lateinit  var webView: WebView
    var context = this
    lateinit var bottomnavigation: BottomNavigationView






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        bottomnavigation  = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomnavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
        openFragment(HomeFragment.newInstance("", ""))
        drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val toggle : ActionBarDrawerToggle = ActionBarDrawerToggle(
                this,
                drawer_layout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        drawer_layout.setDrawerListener(toggle)

        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()


        navigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener(this)

//        show_Home()


    }

//    override
//    fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//
//        //bind searchable configs with the searchview
//        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
//        searchView = menu.findItem(R.id.action_search).actionView as SearchView
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//        searchView.setMaxWidth(Int.MAX_VALUE)
//
//        //implement a listener
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                adapter.getFilter().filter(query)
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                adapter.getFilter().filter(newText)
//                return false
//            }
//        })
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_search) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        var theLink: String? = ""
        var theLinkTitle = ""
        when (menuItem.itemId) {
            R.id.nav_about -> {
                show_About()

            }
            R.id.logOut -> {
                logout()
//                Toast.makeText(this, "Android Store", Toast.LENGTH_SHORT).show()
            }
            R.id.profile -> {
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
            }
            R.id.legal_content_policy -> {
                theLink = CONF_WEB_POLICY_CONTENT
                theLinkTitle = "User Content Policy"
                displayWebView(theLink, theLinkTitle)
            }
            R.id.nav_dashboard -> {
                theLink = CONF_WEB_DASHBOARD
                theLinkTitle = "Nuru Data Dashboard"
                displayWebView(theLink, theLinkTitle)
            }

            R.id.legal_privacy -> {
                theLink = CONF_WEB_POLICY_PRIVACY
                theLinkTitle = "User Privacy Policy"
                displayWebView(theLink, theLinkTitle)
            }
            R.id.legal_terms -> {
                theLink = CONF_WEB_POLICY_TERMS
                theLinkTitle = "Terms of Use"
                displayWebView(theLink, theLinkTitle)
            }


        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

     fun asyncUpdateResponse(result: String) {

     }





     fun displayWebView(the_link: String?, the_title: String?) {

          webView = WebView(context)

         /* RAGE - MAY 25
         * Webview Settings */
         val webSettings: WebSettings = webView.settings
         webSettings.javaScriptEnabled = true
         webSettings.domStorageEnabled = true
         webView.webViewClient = WebViewClient()
         /* @@ RAGE - End Settings */webView.loadUrl(the_link)
         val builder = AlertDialog.Builder(context)
         builder.setTitle(the_title)
                 .setView(webView)
                 .setCancelable(false).setNegativeButton(
                         R.string.lbl_close,
                         DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
//         .setNeutralButton("OK", null)
         //builder.show();
         val alertDialog = builder.create()
         alertDialog.show()

         /*alertDialogBuilder.setMessage(alertText).setCancelable(false).setNegativeButton(R.string.lbl_close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });*/
     }

    fun  show_About()
    {
        val jsonArray: JSONArray = JSONArray()

        /*func_showToast(this, "jsonArray " + String.valueOf(jsonArray.length()));*/


        /*func_showToast(this, "jsonArray " + String.valueOf(jsonArray.length()));*/
        val message_title = "Welcome to Nuru.live"
        var message_extra = ""

        val jsonRecs: Int = jsonArray.length()
        if (jsonRecs == 0) {
            message_extra = """
                
                
                Click on   '+'  below to submit a new report.
                """.trimIndent()
        }

        val message = """
            Nuru is helping real time reporting.
            
            Nuru - Kiswahili for 'Light' (nuru.live) – is a platform that allows community monitors around the world to make observations about social and human rights.$message_extra
            """.trimIndent()

        val dialog_welcome = Dialog(context)
        dialog_welcome.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_welcome.setContentView(R.layout.dialog_welcome)

        val wd_toolbar: Toolbar = dialog_welcome.findViewById(R.id.wd_toolbar)
        wd_toolbar.title = message_title
        wd_toolbar.isEnabled = false

        val txt_wd_text = dialog_welcome.findViewById<TextView>(R.id.wd_text)
        txt_wd_text.text = message
        val btn_wd_button_ok = dialog_welcome.findViewById<Button>(R.id.wd_button_ok)


        btn_wd_button_ok.setOnClickListener { dialog_welcome.dismiss() }
        dialog_welcome.show()



    }
    private fun show_Login() {
        val intent1 = Intent(baseContext, LoginActivity::class.java)
        intent1.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        startActivity(intent1)
        finish()
    }

    fun logout() {
        val preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Oh noo!...")
        alertDialogBuilder.setMessage("Signing out. Confirm!").setCancelable(false).setPositiveButton(
                R.string.lbl_yes
        ) { dialog, id ->
            dialog.cancel()
            lateinit var firebaseAuth: FirebaseAuth
            firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signOut()
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.clear().apply()
            show_Login()
        }.setNegativeButton(R.string.lbl_no) { dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    var navigationItemSelectedListener:BottomNavigationView.OnNavigationItemSelectedListener = object: BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(@NonNull item: MenuItem):Boolean {
            when (item.itemId) {
                R.id.navigation_home -> {
                    openFragment(HomeFragment.newInstance("", ""))

                    return true
                }
                R.id.navigation_initiatives -> {
                    openFragment(InitiativesFragment.newInstance())
                    return true
                }
                R.id.navigation_notifications -> {
                    openFragment(ProfileFragment.newInstance("", ""))
                    return true
                }
            }
            return false
        }
    }
    fun buttonClick(v: View) {
        when (v.id) {
            R.id.add_initiative -> {
                val myIntent = Intent(baseContext, AddInitiative::class.java)

                // for ex: your package name can be "com.example"
                // your activity name will be "com.example.Contact_Developer"
                startActivity(myIntent)
            }
        }
    }
    fun buttonClick1(v: View) {
        when (v.id) {
            R.id.add_report -> {
                val myIntent = Intent(baseContext, PostActivity::class.java)

                // for ex: your package name can be "com.example"
                // your activity name will be "com.example.Contact_Developer"
                startActivity(myIntent)
            }
        }
    }






}