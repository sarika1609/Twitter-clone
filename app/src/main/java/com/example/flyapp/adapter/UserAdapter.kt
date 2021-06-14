package com.example.flyapp.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flyapp.R
import com.example.flyapp.model.User
import com.example.flyapp.ui.profile.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class UserAdapter(private var list: List<User>,context: Context): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val context: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {


        val firebaseUser = FirebaseAuth.getInstance().currentUser

        val user:User = list[position]
        holder.follow.visibility = View.VISIBLE

        holder.name.text = user.name

        Glide.with(context)
            .load(user.imageUrl)
            .into(holder.image)

        isFollowing(user.id, holder.follow)

        if (user.id == firebaseUser?.uid){
            holder.follow.visibility = View.GONE
        }

        holder.itemView.setOnClickListener(View.OnClickListener {

            val editor: SharedPreferences.Editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileid", user.id)
            editor.apply()

            val fragment = ProfileFragment()
            (context as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit()

        })

        holder.follow.setOnClickListener(View.OnClickListener {

            if (holder.follow.text.toString() == "follow") {
                FirebaseDatabase.getInstance().reference.child("Follow").child(firebaseUser!!.uid)
                    .child(
                        "following"
                    )
                    .child(user.id).setValue(true)

                FirebaseDatabase.getInstance().reference.child("Follow").child(firebaseUser!!.uid)
                    .child(
                        "followers"
                    )
                    .child(user.id).setValue(true)
            } else {
                FirebaseDatabase.getInstance().reference.child("Follow").child(firebaseUser!!.uid)
                    .child(
                        "following"
                    )
                    .child(user.id).removeValue()

                FirebaseDatabase.getInstance().reference.child("Follow").child(firebaseUser!!.uid)
                    .child(
                        "followers"
                    )
                    .child(user.id).removeValue()
            }

        })

    }

    override fun getItemCount(): Int {
        return list.size
    }

   class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {

       var name: TextView = view.findViewById<TextView>(R.id.tvUser)
       var image: ImageView = view.findViewById<ImageView>(R.id.profileImage)
       var follow: Button = view.findViewById<Button>(R.id.btnFollow)

   }

    fun isFollowing(userId: String, button: Button){

        var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        val reference:DatabaseReference = FirebaseDatabase.getInstance().reference.child("Follow")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("following")

        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.child(userId).exists()) {
                    button.text = "following"
                } else {
                    button.text = "follow"
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

    }

}