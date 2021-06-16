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

        val userName = findViewById<EditText>(R.id.etSignUpUserName)
        val fullName = findViewById<EditText>(R.id.etSignUpFullName)
        val email = findViewById<EditText>(R.id.etSignUpEmail)
        val password = findViewById<EditText>(R.id.etSignUpPassword)
        val next = findViewById<Button>(R.id.btnSignUpNext)

        firebaseAuth = FirebaseAuth.getInstance()

        next.setOnClickListener(View.OnClickListener {

            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("loading...")
            progressDialog.show()

            val userName = userName.text.toString()
            val fullName = fullName.text.toString()
            val email = email.text.toString()
            val password = password.text.toString()

            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

                Toast.makeText(this, "all fields are required", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "password must have 6 character", Toast.LENGTH_SHORT).show()
            } else {
                register(userName, fullName, email, password)
            }
        })
    }

    fun register(user_name: String, full_name: String, email: String, password: String) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
            OnCompleteListener { it1 ->

                if (it1.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val uId:String = user!!.uid

                    databaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(uId)

                    val hashMap = HashMap<String, Any>()

                    hashMap["id"] = uId
                    hashMap["userName"] = user_name.toLowerCase()
                    hashMap["fullName"] = full_name
                    hashMap["imageUrl"] = "https://firebasestorage.googleapis.com/v0/b/flyapp-660c7.appspot.com/o/profile-user.svg?alt=media&token=367ff2f1-b883-46ef-b910-ae60f89307c4"

                    databaseReference.setValue(hashMap).addOnCompleteListener(OnCompleteListener {

                        if (it.isSuccessful) {
                            progressDialog.dismiss()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }


                    })
                } else {
                    Toast.makeText(this, "invalid email or password", Toast.LENGTH_SHORT).show()

                }

            })

    }
}