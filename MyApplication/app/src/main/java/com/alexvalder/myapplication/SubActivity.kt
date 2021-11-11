package com.alexvalder.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class SubActivity : AppCompatActivity() {
    private lateinit var radioGroupStations: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        radioGroupStations = findViewById(R.id.availableStations)
        radioGroupStations.apply {
            Stations.values().onEachIndexed { index, station ->
                addView(
                    RadioButton(this@SubActivity).apply {
                        text = station.name
                        id = index
                        isChecked = CommonValues.currentStation == station
                    }
                )
            }
            setOnCheckedChangeListener { _, i ->
                CommonValues.currentStation = Stations.ofIndex(i)
                Toast.makeText(
                    this@SubActivity,
                    "You just arrived at ${CommonValues.currentStation}",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}