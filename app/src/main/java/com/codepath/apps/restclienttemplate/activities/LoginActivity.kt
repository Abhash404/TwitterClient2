package com.codepath.apps.restclienttemplate.activities

import android.content.Intent
import android.os.AsyncTask.execute
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.codepath.apps.restclienttemplate.R
import com.codepath.apps.restclienttemplate.TwitterClient
import com.codepath.apps.restclienttemplate.backend.TwitterApplication
import com.codepath.apps.restclienttemplate.models.SampleModel
import com.codepath.apps.restclienttemplate.models.SampleModelDao
import com.codepath.oauth.OAuthLoginActionBarActivity

class LoginActivity : OAuthLoginActionBarActivity<TwitterClient>() {

    private var sampleModelDao: SampleModelDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val sampleModel = SampleModel()
        sampleModel.name = "CodePath"
        sampleModelDao = (applicationContext as TwitterApplication).myDatabase?.sampleModelDao()
        execute { sampleModelDao?.insertModel(sampleModel) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_login, menu)
        return true
    }

    override fun onLoginSuccess() {

        Log.i("Bruce", "Success.")
        val i = Intent(this, TimelineActivity::class.java)
        startActivity(i)
    }

    override fun onLoginFailure(e: Exception) {
        Log.i("Bruce", "Error Occurred! PLease Try Again.")
        e.printStackTrace()
    }

    fun loginToRest(view: View?) {

        client.connect()
    }
}