package com.example.flyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemReselectedListener,
    NavigationView.OnNavigationItemSelectedListener {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navController: NavController

    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        navController = Navigation.findNavController(this, R.id.frame_layout)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigation_menu)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener(this)

        NavigationUI.setupWithNavController(bottomNavigationView, navController)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true
        }
        return true
    }

    override fun onNavigationItemReselected(item: MenuItem) {

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.navigation_profile -> {
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show()
            }
            R.id.navigation_list -> {
                Toast.makeText(this, "list", Toast.LENGTH_SHORT).show()
            }
            R.id.navigation_topic -> {
                Toast.makeText(this, "topic", Toast.LENGTH_SHORT).show()
            }
            R.id.navigation_bookmark -> {
                Toast.makeText(this, "bookmark", Toast.LENGTH_SHORT).show()
            }
            R.id.navigation_moment -> {
                Toast.makeText(this, "moment", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

}