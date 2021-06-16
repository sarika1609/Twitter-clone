package com.example.flyapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar!!.hide()

        firebaseAuth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.etLoginEmail)
        val password = findViewById<EditText>(R.id.etLoginPassword)
        val createAccount = findViewById<TextView>(R.id.tvCreateAccount)
        val next = findViewById<Button>(R.id.btnLoginNext)


        createAccount.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        })

        next.setOnClickListener(View.OnClickListener {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("loading...")
            progressDialog.show()

            val email = email.text.toString()
            val password = password.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "all fields are required", Toast.LENGTH_SHORT).show()
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                    OnCompleteListener {

                        if (it.isSuccessful) {
                            var databaseReference =
                                FirebaseDatabase.getInstance().reference.child("Users")
                                    .child(firebaseAuth.currentUser!!.uid)

                            databaseReference.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {

                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    progressDialog.dismiss()
                                }
                            })
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(this, "authentication failed", Toast.LENGTH_SHORT).show()
                        }

                    })
            }
        })
    }
}