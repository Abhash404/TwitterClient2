package com.codepath.apps.restclienttemplate.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.codepath.apps.restclienttemplate.R
import com.codepath.apps.restclienttemplate.TwitterClient
import com.codepath.apps.restclienttemplate.backend.TwitterApplication
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.material.snackbar.Snackbar
import okhttp3.Headers


class ComposeActivity : AppCompatActivity() {
    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var client: TwitterClient
    lateinit var charLimit: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)
        val toolbar: Toolbar = findViewById(R.id.include)
        setSupportActionBar(toolbar)
        supportActionBar?.hide()

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        client = TwitterApplication.getRestClient(this)
        charLimit = findViewById(R.id.tvCharLimit)
        charLimit.text = MAX_CHAR.toString()

        etCompose.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                charLimit.text = (MAX_CHAR - s.length).toString()

                if (s.length <= 280) {

                    charLimit.setTextColor(Color.WHITE)

                }

                else {

                    charLimit.setTextColor(Color.RED)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        btnTweet.setOnClickListener {

            val tweetContent = etCompose.text.toString()

            when {

                tweetContent.isEmpty() -> Snackbar.make(it, "Please Type something before Tweeting!", Snackbar.LENGTH_SHORT).show()

                tweetContent.length > 280 -> Snackbar.make(it, "Characters cannot exceed beyond 280!", Snackbar.LENGTH_SHORT).show()

                else ->

                    client.publishTweet(tweetContent, object : JsonHttpResponseHandler() {
                        override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {

                            Log.e(TAG, "Error Occurred! Please try again.", throwable)
                        }

                        override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {

                            Log.i(TAG, "Success.")

                            val tweet = Tweet.fromJson(json.jsonObject)
                            val i = Intent()
                            i.putExtra("tweet", tweet)
                            setResult(RESULT_OK, i)
                            finish()
                        }
                    })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_compose, menu)
        return true
    }

    companion object {

        const val TAG = "ComposeActivity"
        const val MAX_CHAR = 280
    }
}