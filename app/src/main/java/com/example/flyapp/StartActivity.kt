package com.example.flyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class StartActivity : AppCompatActivity() {

    private val splashTimeOut :Int = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        supportActionBar!!.hide()

        Handler().postDelayed({
            val intent = Intent(this@StartActivity, CreateAccountActivity::class.java)

            startActivity(intent)
            finish()
        }, splashTimeOut.toLong())

    }
}