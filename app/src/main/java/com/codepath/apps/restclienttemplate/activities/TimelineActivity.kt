package com.codepath.apps.restclienttemplate.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.EndlessRecyclerViewScrollListener
import com.codepath.apps.restclienttemplate.R
import com.codepath.apps.restclienttemplate.TweetsAdapter
import com.codepath.apps.restclienttemplate.TwitterClient
import com.codepath.apps.restclienttemplate.backend.TwitterApplication
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.Headers
import org.json.JSONException


class TimelineActivity : AppCompatActivity() {

    private lateinit var client: TwitterClient
    private lateinit var rvTweets: RecyclerView
    lateinit var adapter: TweetsAdapter
    val tweets = ArrayList<Tweet>()
    lateinit var swipeContainer: SwipeRefreshLayout
    private var minTweetId: Long = -1

    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        val logo: ImageView = findViewById(R.id.twitterLogoBlue)
        logo.visibility = View.VISIBLE

        val composeBtn: FloatingActionButton = findViewById(R.id.compose)

        swipeContainer = findViewById(R.id.swipeContainer)

        swipeContainer.setOnRefreshListener {

            Log.i(TAG, "Loading...")
            populateHomeTimeline()
        }

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light)

        client = TwitterApplication.getRestClient(this)
        rvTweets = findViewById(R.id.rvTweets)
        adapter = TweetsAdapter(tweets)

        val linearLayoutManager = LinearLayoutManager(this)
        rvTweets.layoutManager = linearLayoutManager

        val dividerItemDecoration = DividerItemDecoration(rvTweets.context, linearLayoutManager.orientation)
        rvTweets.addItemDecoration(dividerItemDecoration)
        rvTweets.adapter = adapter
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {

                loadMoreData(totalItemsCount)
            }
        }

        rvTweets.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)

        toolbar.setOnClickListener {

            rvTweets.smoothScrollToPosition(0)
        }

        composeBtn.setOnClickListener{

            val i = Intent(this, ComposeActivity::class.java)
            startActivityForResult(i, REQUEST_CODE)
        }

        populateHomeTimeline()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout) {

            logout()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            val tweet = data?.getParcelableExtra<Tweet>("tweet") as Tweet

            tweets.add(0, tweet)
            adapter.notifyItemInserted(0)
            rvTweets.smoothScrollToPosition(0)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun loadMoreData(offset: Int) {

        client.getNextPageOfTweets(object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {

                Log.i(TAG, "loadMoreData onSuccess!")
                val jsonArray = json.jsonArray

                try {

                    val listOfNewTweetsRetrieved = Tweet.fromJsonArray(jsonArray)

                    adapter.addAll(listOfNewTweetsRetrieved)
                    minTweetId = tweets[tweets.size - 1].uid
                    Log.i(TAG, "Current adapter size is ${adapter.itemCount}")

                }

                catch (e: JSONException) {

                    Log.e(TAG, "JSON Exception $e")
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {

                Log.i(TAG, "onFailure $statusCode")
            }

        }, minTweetId)
    }

    private fun populateHomeTimeline() {

        client.getHomeTimeline(object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {

                Log.i(TAG, "Populate onSuccess!")

                val jsonArray = json.jsonArray

                try {

                    adapter.clear()

                    val listOfNewTweetsRetrieved = Tweet.fromJsonArray(jsonArray)

                    adapter.addAll(listOfNewTweetsRetrieved)
                    minTweetId = tweets[tweets.size - 1].uid
                    swipeContainer.isRefreshing = false
                    scrollListener?.resetState()
                    Log.i(TAG, "Current adapter size is ${adapter.itemCount}")

                }

                catch (e: JSONException) {

                    Log.e(TAG, "JSON Exception $e")
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {

                Log.i(TAG, "onFailure $statusCode")
            }
        })
    }

    private fun logout() {

        client.clearAccessToken()
        Log.i(TAG, "Cleared!")

        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }

    companion object {

        const val TAG = "TimelineActivity"
        const val REQUEST_CODE = 10
    }
}