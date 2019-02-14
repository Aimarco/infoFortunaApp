package com.example.temporal.infofortuna

import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.widget.ProgressBar
import android.os.Bundle
import android.app.Activity


class SplashScreen : Activity() {
    private var mProgress: ProgressBar? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Show the splash screen
        setContentView(R.layout.launch_screen)
        mProgress = findViewById(R.id.splashProgress)

        // Start lengthy operation in a background thread
        Thread(Runnable {
            doWork()
            startApp()
            finish()
        }).start()
    }

    private fun doWork() {
        var progress = 0
        while (progress < 100) {
            try {
                Thread.sleep(1000)
                mProgress!!.progress = progress
            } catch (e: Exception) {
                e.printStackTrace()
            }

            progress += 25
        }
    }

    private fun startApp() {
        val intent = Intent(this@SplashScreen, MainActivity::class.java)
        startActivity(intent)
    }
}