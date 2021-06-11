package com.example.flyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim) }

  private var clicked = false

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
        if(!clicked){
            fabThree.startAnimation(fromBottom)
            fabTwo.startAnimation(fromBottom)
            fabOne.startAnimation(fromBottom)
            fabMain.startAnimation(rotateOpen)
        }else{
            fabThree.startAnimation(toBottom)
            fabTwo.startAnimation(toBottom)
            fabOne.startAnimation(toBottom)
            fabMain.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked){
            fabThree.visibility = View.VISIBLE
            fabTwo.visibility = View.VISIBLE
            fabOne.visibility = View.VISIBLE
        }else{
            fabThree.visibility = View.INVISIBLE
            fabTwo.visibility = View.INVISIBLE
            fabOne.visibility = View.INVISIBLE
        }
    }
}