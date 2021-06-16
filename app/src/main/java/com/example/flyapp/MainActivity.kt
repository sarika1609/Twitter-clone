package com.example.flyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemReselectedListener,
    NavigationView.OnNavigationItemSelectedListener {


    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }

    private var clicked = false

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navController: NavController

    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabMain.setOnClickListener {
            onAddButtonClicked()
        }
        fabOne.setOnClickListener {

        }
        fabTwo.setOnClickListener {

        }
        fabThree.setOnClickListener {

        }

    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            fabThree.startAnimation(fromBottom)
            fabTwo.startAnimation(fromBottom)
            fabOne.startAnimation(fromBottom)
            fabMain.startAnimation(rotateOpen)
        } else {
            fabThree.startAnimation(toBottom)
            fabTwo.startAnimation(toBottom)
            fabOne.startAnimation(toBottom)
            fabMain.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            fabThree.visibility = View.VISIBLE
            fabTwo.visibility = View.VISIBLE
            fabOne.visibility = View.VISIBLE
        } else {
            fabThree.visibility = View.INVISIBLE
            fabTwo.visibility = View.INVISIBLE
            fabOne.visibility = View.INVISIBLE
        }


        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        navController = Navigation.findNavController(this, R.id.frame_layout)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigation_menu)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener(this)

        NavigationUI.setupWithNavController(bottomNavigationView, navController)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
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