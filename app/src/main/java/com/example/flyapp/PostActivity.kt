package com.example.flyapp

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.theartofdev.edmodo.cropper.CropImage
import org.jetbrains.anko.find

class PostActivity : AppCompatActivity() {

    lateinit var imageUri: Uri
    lateinit var imageUrl: String
    lateinit var storageReference: StorageReference
    lateinit var mEtDescription: EditText
    lateinit var mClose: ImageView
    lateinit var mImageAdded: ImageView
    lateinit var mTvPost: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        mClose = findViewById(R.id.close)
        mEtDescription = findViewById(R.id.etDescription)
        mImageAdded = findViewById(R.id.image_added)
        mTvPost = findViewById(R.id.tweet)

        storageReference = FirebaseStorage.getInstance().getReference("posts")

        mClose.setOnClickListener(View.OnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        })

        mTvPost.setOnClickListener(View.OnClickListener {
            uploadImage()
        })
        CropImage.activity().setAspectRatio(1, 1).start(this)

    }

    private fun getFileExtension(uri: Uri): String {
        val contentResolver = contentResolver
        val mime = MimeTypeMap.getSingleton()

        return mime.getExtensionFromMimeType(contentResolver.getType(uri))!!
    }

    private fun uploadImage() {

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("posting...")
        progressDialog.show()

        if (imageUri != null) {
            val reference =
                storageReference.child("${System.currentTimeMillis()} . ${getFileExtension(imageUri)}")

            val uploadTask = reference.putFile(imageUri)

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@continueWithTask reference.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    imageUrl = downloadUri.toString()

                    val databaseReference = FirebaseDatabase.getInstance().getReference("posts")
                    val postId: String = databaseReference.push().key!!
                    val hashMap = HashMap<String, Any>()

                    hashMap["postId"] = postId
                    hashMap["postImage"] = imageUrl
                    hashMap["description"] = mEtDescription.text.toString()
                    hashMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid

                    databaseReference.child(postId).setValue(hashMap)
                    progressDialog.dismiss()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this, "no image selected", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                mImageAdded.setImageURI(resultUri)
            } else {
                Toast.makeText(this, "searching went wrong", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}