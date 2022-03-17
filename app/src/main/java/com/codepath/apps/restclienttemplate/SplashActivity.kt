package com.codepath.apps.restclienttemplate

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.codepath.apps.restclienttemplate.activities.LoginActivity

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    var splashProgress: ProgressBar? = null
    var SPLASH_TIME = 5000

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        splashProgress = findViewById(R.id.splashProgress)
        playProgress()

        Handler().postDelayed({

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()

        }, SPLASH_TIME.toLong())
    }

    private fun playProgress() {

        ObjectAnimator.ofInt(splashProgress, "progress", 100)
            .setDuration(5000)
            .start()
    }
}