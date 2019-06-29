package com.larvaesoft.opengst

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.larvaesoft.opengst.ui.main.MainActivity
import org.jetbrains.anko.intentFor

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onStart() {
        super.onStart()
        startActivity(intentFor<MainActivity>())
        finish()
    }
}
