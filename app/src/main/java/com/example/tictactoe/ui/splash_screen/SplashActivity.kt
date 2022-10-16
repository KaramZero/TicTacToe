package com.example.tictactoe.ui.splash_screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.MainActivity
import com.example.tictactoe.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        Handler(Looper.myLooper()!!).postDelayed({ // This method will be executed once the timer is over
            val i = Intent(this@SplashActivity, MainActivity::class.java)

            startActivity(i)
            finish()
        }, 1000)

    }
}