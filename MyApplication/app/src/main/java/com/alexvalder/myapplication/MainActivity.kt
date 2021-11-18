package com.alexvalder.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.alexvalder.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Main activity of the application and start screen.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var textList: LinearLayout

    private val scope = CoroutineScope(Dispatchers.IO)


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        textList = findViewById(R.id.testList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.refresh -> {
            updateGui()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun updateGui() {
        if (textList.childCount > 0) {
            textList.removeAllViews()
        }

        try {
            val json = runBlocking(Dispatchers.IO) {
                val url = URL(BANK_URI)
                val connection = url.openConnection() as HttpURLConnection

                val lines = if (connection.responseCode in 100..399) {
                    BufferedReader(InputStreamReader(connection.inputStream))
                } else {
                    BufferedReader(InputStreamReader(connection.errorStream))
                }.readLines().joinToString(
                    separator = "",
                    prefix = "",
                    postfix = "",
                )

                Json.parseToJsonElement(lines).jsonArray
            }

            /*
             r030 -> number
             txt -> name
             rate -> ???
             cc -> code
             exchangedate -> today's date
             */

            for (entry in json.asIterable()) {
                val map = entry.jsonObject.toMap()

                textList.addView(
                    TextView(this@MainActivity).apply {
                        text = "${map["r030"]} : ${map["txt"]} : ${map["cc"]} : ${map["rate"]}"
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }
                )
            }
        } catch (thr: Throwable) {
            Log.e(TAG, "Fuck", thr)
        }
    }

    companion object {
        private const val TAG = "MainApplication"
        private const val BANK_URI =
            "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchangenew?json"
    }
}
