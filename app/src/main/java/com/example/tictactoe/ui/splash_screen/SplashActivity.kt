package com.example.tictactoe.ui.splash_screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.MainActivity
import com.example.tictactoe.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.myLooper()!!).postDelayed({ // This method will be executed once the timer is over
            val i = Intent(this@SplashActivity, MainActivity::class.java)

            startActivity(i)
            finish()
        }, 2000)

        val imgAnim1 = binding.animationImageView1
        val imgAnim2 = binding.animationImageView2

        val runnableAnim = Runnable{
            imgAnim1.visibility = View.VISIBLE
            imgAnim2.visibility = View.VISIBLE
            kotlin.run {
                imgAnim1.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1500).withEndAction {
                    kotlin.run {
                        imgAnim1.scaleX = 1f
                        imgAnim1.scaleY = 1f
                        imgAnim1.alpha = 1f
                    }
                }

                imgAnim2.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000).withEndAction {
                    kotlin.run {
                        imgAnim2.scaleX = 1f
                        imgAnim2.scaleY = 1f
                        imgAnim2.alpha = 1f
                    }
                }
            }
        }

        Handler(Looper.myLooper()!!).postDelayed(runnableAnim,1500)
        runnableAnim.run()

        binding.logoImageView.setOnClickListener {
            it.isClickable = false
            Handler(Looper.myLooper()!!).postDelayed(runnableAnim,1500)
            runnableAnim.run()
            Handler(Looper.myLooper()!!).postDelayed({
                imgAnim1.visibility = View.GONE
                imgAnim2.visibility = View.GONE
                it.isClickable = true
            }, 2500)
        }

    }
}