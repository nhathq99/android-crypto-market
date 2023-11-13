package com.example.cryptomarket.view.ui.onboarding

import android.content.Intent
import android.os.Bundle
import com.example.cryptomarket.bases.BaseActivity
import com.example.cryptomarket.databinding.ActivityOnboardingBinding
import com.example.cryptomarket.utils.SharePrefUtils
import com.example.cryptomarket.view.ui.main.MainActivity

class OnboardingActivity : BaseActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashGetStarted.setOnClickListener {
            SharePrefUtils.setIgnoreOnBoarding(this, true)
            intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
            startActivity(intent)
            this.finish()
        }
    }
}