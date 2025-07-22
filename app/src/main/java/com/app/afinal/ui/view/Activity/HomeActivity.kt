package com.app.afinal.ui.view.Activity

import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.app.afinal.Application.MySharePreferences
import com.app.afinal.databinding.ActivityHomeBinding
import com.app.afinal.ui.view.BaseActivity
import com.app.afinal.ui.view.MainActivity

class HomeActivity: BaseActivity<ActivityHomeBinding>() {

    override fun provideBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            false
        }
        super.onCreate(savedInstanceState)
        toolbar()
        val mySharePreferences = MySharePreferences(applicationContext)
        val token = mySharePreferences.getToken()
        binding.btnGetStarted.setOnClickListener {
            navigateToNextScreen(token)
        }
    }
    fun navigateToNextScreen(token : String ? = null){
        if (token.isNullOrEmpty()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}