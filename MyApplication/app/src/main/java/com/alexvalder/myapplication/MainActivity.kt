package com.alexvalder.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.provider.BaseColumns
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.view.children
import com.alexvalder.myapplication.databinding.ActivityMainBinding

/**
 * Main activity of the application and start screen.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var linearLayout: LinearLayout

    private lateinit var dbHelper: ReaderContract.ReaderDbHelper

    private val ids = mutableListOf<Int>()
    private var selectedItem: Int = -1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        dbHelper = ReaderContract.ReaderDbHelper(this)

        prepareDb()
        prepareGui()
    }

    private fun prepareDb() {
        val db = dbHelper.writableDatabase
        repeat(10) {
            db.insert(
                ReaderContract.Entry.TABLE_NAME,
                null,
                ContentValues().apply {
                    put(ReaderContract.Entry.COLUMN_NAME_TITLE, "Item $it")
                })
        }
    }

    private fun prepareGui() {
        linearLayout = findViewById(R.id.testList)
        linearLayout.apply {
            children.forEach { unregisterForContextMenu(it) }
            removeAllViews()
        }

        ids.clear()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            ReaderContract.Entry.TABLE_NAME,
            arrayOf(
                BaseColumns._ID,
                ReaderContract.Entry.COLUMN_NAME_TITLE,
            ),
            null,
            null,
            null,
            null,
            null,
        )
        with(cursor) {
            while (moveToNext()) {
                linearLayout.addView(
                    TextView(this@MainActivity).also {
                        ids.add(getInt(getColumnIndexOrThrow(BaseColumns._ID)))
                        it.text = getString(
                            getColumnIndexOrThrow(ReaderContract.Entry.COLUMN_NAME_TITLE)
                        )
                        it.textAlignment = View.TEXT_ALIGNMENT_CENTER
                        this@MainActivity.registerForContextMenu(it)
                    }
                )
            }
        }
        cursor.close()
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        (v as? TextView)?.let {
            menu.setHeaderTitle(it.text)
        }

        (v.parent as? LinearLayout)?.let {
            selectedItem = ids[it.indexOfChild(v)]
        }

        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            "Remove" -> {
                if (selectedItem != -1) {
                    dbHelper.writableDatabase.delete(
                        ReaderContract.Entry.TABLE_NAME,
                        "${BaseColumns._ID} = ?",
                        arrayOf("$selectedItem")
                    )
                }
                prepareGui()
                selectedItem = -1
                return true
            }
        }
        selectedItem = -1
        return super.onContextItemSelected(item)
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "MainApplication"
        private const val DATABASE = "test.db"
    }
}
