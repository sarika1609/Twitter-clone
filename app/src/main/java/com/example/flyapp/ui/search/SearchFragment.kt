package com.example.flyapp.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flyapp.R
import com.example.flyapp.adapter.UserAdapter
import com.example.flyapp.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class SearchFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var userAdapter: UserAdapter
    var list  = mutableListOf<User>()
    lateinit var searchBar: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)

        userAdapter = UserAdapter(list,this.requireContext())

        recyclerView.adapter = userAdapter

        searchBar = view.findViewById(R.id.searchBar)

        readUsers()
        searchBar.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchUser(s.toString().toLowerCase())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        return view
    }

    private fun searchUser(s:String){

        val query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("name")
            .startAt(s)
            .endAt("$s\uf8ff")

        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                list.clear()
               for (item in snapshot.children){
                   val user : User? = item.getValue(User::class.java)
                   list.add(user!!)
               }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"ERROR",Toast.LENGTH_SHORT).show()

            }
        })

    }
    private fun readUsers(){
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (searchBar.text.toString()==""){
                    list.clear()
                }

                for (item in snapshot.children){
                    val user = item.getValue(User::class.java)
                    list.add(user!!)
                }
                userAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"ERROR", Toast.LENGTH_SHORT).show()

            }
        })
    }


}