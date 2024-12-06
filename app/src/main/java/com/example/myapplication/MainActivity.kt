package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setAppTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonOpenPlanner = findViewById<Button>(R.id.button_open_planner)
        buttonOpenPlanner.setOnClickListener {
            val intent = Intent(this, EventPlannerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setAppTheme() {
        val sharedPref = getSharedPreferences("ThemePref", Context.MODE_PRIVATE)
        val isDarkTheme = sharedPref.getBoolean("isDarkTheme", false)
        if (isDarkTheme) {

        } else {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_change_theme -> {

                true
            }
            R.id.menu_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}