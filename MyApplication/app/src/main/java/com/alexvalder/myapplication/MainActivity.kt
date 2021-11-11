package com.alexvalder.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.alexvalder.myapplication.databinding.ActivityMainBinding

/**
 * Main activity of the application and start screen.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currentStationText: TextView
    private lateinit var buttonEnter: Button
    private lateinit var buttonLeave: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        currentStationText = findViewById(R.id.textView)
        currentStationText.apply {
            text = "Current station: ${CommonValues.currentStation}"
        }

        buttonEnter = findViewById(R.id.buttonLeft)
        buttonEnter.apply {
            isEnabled = true
            setOnClickListener {
                enterTrain()
                it.isEnabled = false
                buttonLeave.isEnabled = true
            }
        }

        buttonLeave = findViewById(R.id.buttonRight)
        buttonLeave.apply {
            isEnabled = false
            setOnClickListener {
                leaveTrain()
                it.isEnabled = false
                buttonEnter.isEnabled = true
            }
        }
    }
    
    @SuppressLint("SetTextI18n")
    override fun onResume() {
        currentStationText.apply {
            text = "Current station: ${CommonValues.currentStation}"
        }
        super.onResume()
    }

    private fun enterTrain() {
        CommonValues.onTrain = true
        Toast.makeText(
            this,
            "You just entered the train",
            Toast.LENGTH_SHORT,
        ).show()
    }

    private fun leaveTrain() {
        CommonValues.onTrain = false
        Toast.makeText(
            this,
            "You just left the train",
            Toast.LENGTH_SHORT,
        ).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            if (CommonValues.onTrain) {
                startActivity(Intent(this, SubActivity::class.java))
            } else {
                Toast.makeText(
                    this,
                    "Please enter the train first",
                    Toast.LENGTH_SHORT,
                ).show()
            }
            true
        }
        R.id.some_clickable -> {
            Toast.makeText(
                this,
                "YOU DID IT! :O",
                Toast.LENGTH_SHORT
            ).show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "MainApplication"
    }
}
