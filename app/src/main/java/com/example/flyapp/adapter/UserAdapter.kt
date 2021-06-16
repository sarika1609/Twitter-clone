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

class UserAdapter(private var userLst: List<User>,context: Context): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val mContext: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {


        val firebaseUser = FirebaseAuth.getInstance().currentUser

        val user:User = userLst[position]
        holder.follow.visibility = View.VISIBLE

        holder.userName.text = user.userName
        holder.fullName.text = user.fullName

        Glide.with(mContext)
            .load(user.imageUrl)
            .placeholder(R.drawable.ic_profile_user)
            .into(holder.profileImage)

        isFollowing(user.id, holder.follow)

        if (user.id == firebaseUser?.uid){
            holder.follow.visibility = View.GONE
        }

        holder.itemView.setOnClickListener(View.OnClickListener {

            val editor: SharedPreferences.Editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            editor.putString("profileid", user.id)
            editor.apply()

            val fragment = ProfileFragment()
            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
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
        return userLst.size
    }

   class UserViewHolder(view: View): RecyclerView.ViewHolder(view) {

       val userName = view.findViewById<TextView>(R.id.user_item_user_name) as TextView
       val fullName = view.findViewById<TextView>(R.id.user_item_full_name) as TextView
       val profileImage = view.findViewById<ImageView>(R.id.user_item_profile_image) as ImageView
       val follow = view.findViewById<Button>(R.id.user_item_button_follow) as Button

   }

    fun isFollowing(userId: String, button: Button){

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
                Toast.makeText(mContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

    }

}