package com.alexvalder.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import com.alexvalder.myapplication.databinding.ActivityMainBinding

/**
 * Main activity of the application and start screen.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var switch: SwitchCompat
    private lateinit var textView: TextView
    private lateinit var starButton: ImageButton

    private var buttonsPressed: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        textView = findViewById(R.id.testTextView)

        switch = findViewById(R.id.switchCompatText)
        switch.apply {
            setOnCheckedChangeListener { _, checked ->
                textView.text = when (checked) {
                    true -> getString(R.string.text02)
                    false -> getString(R.string.text01)
                }
            }
        }

        starButton = findViewById(R.id.starButton)
        starButton.apply {
            setOnClickListener {
                buttonsPressed += 1
                Toast.makeText(
                    this@MainActivity,
                    "You pressed button $buttonsPressed " +
                            if (buttonsPressed == 1) "time" else "times",
                    Toast.LENGTH_SHORT,
                ).show()
            }
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
}
