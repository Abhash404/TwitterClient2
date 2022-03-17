package com.codepath.apps.restclienttemplate.backend

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.codepath.apps.restclienttemplate.TwitterClient
import com.codepath.oauth.OAuthBaseClient
import com.facebook.stetho.Stetho

class TwitterApplication : Application() {

    var myDatabase: MyDatabase? = null

    override fun onCreate() {

        super.onCreate()

        myDatabase = Room.databaseBuilder(this, MyDatabase::class.java, MyDatabase.NAME).fallbackToDestructiveMigration().build()

        Stetho.initializeWithDefaults(this)
    }

    companion object {

        fun getRestClient(context: Context): TwitterClient {

            return OAuthBaseClient.getInstance(TwitterClient::class.java, context) as TwitterClient
        }
    }
}