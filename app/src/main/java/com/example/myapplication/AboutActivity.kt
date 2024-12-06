package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setAppTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    private fun setAppTheme() {
        val sharedPref = getSharedPreferences("ThemePref", Context.MODE_PRIVATE)
        val isDarkTheme = sharedPref.getBoolean("isDarkTheme", false)
        if (isDarkTheme) {

        } else {

        }
    }
}