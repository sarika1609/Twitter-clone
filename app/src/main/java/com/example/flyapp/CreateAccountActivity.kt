package com.example.flyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.w3c.dom.Text

class CreateAccountActivity : AppCompatActivity() {

    lateinit var firebaseUser: FirebaseUser

//    override fun onStart() {
//        super.onStart()
//
//        firebaseUser = FirebaseAuth.getInstance().currentUser
//
//        if (firebaseUser != null){
//
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        supportActionBar!!.hide()

        val createAccount = findViewById<Button>(R.id.btnCreateAccount)
        val login = findViewById<TextView>(R.id.tvLogin)

        createAccount.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        })

        login.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })
    }
}