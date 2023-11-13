package com.example.cryptomarket.view.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.cryptomarket.databinding.ActivitySplashBinding
import com.example.cryptomarket.utils.SharePrefUtils
import com.example.cryptomarket.view.ui.main.MainActivity
import com.example.cryptomarket.view.ui.onboarding.OnboardingActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        checkOnBoarding()
        super.onResume()
    }

    private fun checkOnBoarding() {
        Handler(Looper.getMainLooper()).postDelayed({
            val ignore = SharePrefUtils.ignoreOnBoarding(this)
            var intent = Intent(this, OnboardingActivity::class.java)
            if (ignore) {
                intent = Intent(this, MainActivity::class.java)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
            startActivity(intent)
            this.finish()
        }, 1500)

    }
}