package com.codepath.apps.restclienttemplate

import android.content.Context
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.oauth.OAuthBaseClient
import com.github.scribejava.apis.TwitterApi

class TwitterClient(context: Context) : OAuthBaseClient(context, REST_API_INSTANCE, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, null, String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host), context.getString(R.string.intent_scheme), context.packageName, FALLBACK_URL)) {

    companion object {

        val REST_API_INSTANCE: TwitterApi = TwitterApi.instance()
        const val REST_URL = "https://api.twitter.com/1.1"
        const val REST_CONSUMER_KEY = BuildConfig.CONSUMER_KEY
        const val REST_CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET
        const val FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html"
        const val REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end"
    }

    fun getHomeTimeline(handler: JsonHttpResponseHandler) {

        val apiUrl = getApiUrl("statuses/home_timeline.json")
        val params = RequestParams()
        params["count_id"] = "25"
        params["since_id"] = "1"
        client.get(apiUrl, params, handler)
    }

    fun publishTweet(tweetContent: String, handler: JsonHttpResponseHandler) {

        val apiUrl = getApiUrl("statuses/update.json")
        val params = RequestParams()
        params["status"] = tweetContent
        client.post(apiUrl, params, "", handler)
    }

    fun getNextPageOfTweets(handler: JsonHttpResponseHandler?, maxId: Long) {

        val apiUrl = getApiUrl("statuses/home_timeline.json")
        val params = RequestParams()
        params["count"] = "25"
        params["max_id"] = (maxId - 1).toString()
        client[apiUrl, params, handler]
    }
}