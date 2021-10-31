package com.alexvalder.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.*
import com.alexvalder.myapplication.databinding.ActivityMainBinding

/**
 * Main activity of the application and start screen.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var image: ImageView
    private lateinit var button1: Button
    private lateinit var button2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val animation1 = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.animation1,
        )

        image = findViewById(R.id.androidImage)

        button1 = findViewById(R.id.buttonLeft)
        button1.setOnClickListener {
            image.startAnimation(animation1)
            Toast.makeText(
                this,
                "Animation started",
                Toast.LENGTH_SHORT,
            ).show()
            button1.isEnabled = false
            button2.isEnabled = true
        }

        button2 = findViewById(R.id.buttonRight)
        button2.isEnabled = false
        button2.setOnClickListener {
            image.clearAnimation()
            Toast.makeText(
                this,
                "Animation was canceled",
                Toast.LENGTH_SHORT,
            ).show()
            button1.isEnabled = true
            button2.isEnabled = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val TAG = "MainApplication"
    }
}
