package com.example.flyapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import java.util.function.ObjDoubleConsumer
import kotlin.collections.HashMap

class SignUpActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar!!.hide()

        val name = findViewById<EditText>(R.id.etName)
        val email = findViewById<EditText>(R.id.etPhoneNumber)
        val password = findViewById<EditText>(R.id.etPassword)
        val next = findViewById<Button>(R.id.btnNext)

        firebaseAuth = FirebaseAuth.getInstance()

        next.setOnClickListener(View.OnClickListener {

            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("loading...")
            progressDialog.show()

            val name = name.text.toString()
            val email = email.text.toString()
            val password = password.text.toString()

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "all fields are required", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "password must have 6 character", Toast.LENGTH_SHORT).show()
            } else {
                register(name, email, password)
            }
        })
    }

    fun register(name: String, email: String, password: String) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
            OnCompleteListener { it1 ->

                if (it1.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val uId = user.uid

                    databaseReference =
                        FirebaseDatabase.getInstance().reference.child("Users").child(uId)

                    val hashMap = HashMap<String, Any>()

                    hashMap["id"] = uId
                    hashMap["imageUrl"] =
                        "https://firebasestorage.googleapis.com/v0/b/flyapp-660c7.appspot.com/o/profile-user.svg?alt=media&token=d65d0e4e-43ac-45ad-8a41-1219465c27f0"

                    databaseReference.setValue(hashMap).addOnCompleteListener(OnCompleteListener {

                        if (it.isSuccessful) {
                            progressDialog.dismiss()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }


                    })
                } else {
                    Toast.makeText(this, "invalid email or password", Toast.LENGTH_SHORT).show()

                }

            })

    }
}