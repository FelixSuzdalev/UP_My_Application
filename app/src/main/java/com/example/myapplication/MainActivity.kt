package com.example.myapplication

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var addEventButton: Button
    private lateinit var selectedDateText: TextView
    private lateinit var eventListView: ListView

    private var selectedDate: String = getCurrentDate()
    private val eventsMap: MutableMap<String, MutableList<String>> = mutableMapOf()
    private val sharedPreferencesKey = "CalendarEvents"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "onCreate: Инициализация приложения началась")

        // Установка Toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        Log.d("MainActivity", "onCreate: Toolbar установлен")

        // Инициализация элементов
        calendarView = findViewById(R.id.calendarView)
        addEventButton = findViewById(R.id.addEventButton)
        selectedDateText = findViewById(R.id.selectedDate)
        eventListView = findViewById(R.id.eventListView)

        // Загрузка событий из SharedPreferences
        try {
            loadEvents()
            Log.d("MainActivity", "onCreate: События загружены: $eventsMap")
        } catch (e: Exception) {
            Log.e("MainActivity", "onCreate: Ошибка при загрузке событий", e)
        }

        // Установка текущей даты
        selectedDateText.text = "Выбранная дата: $selectedDate"
        Log.d("MainActivity", "onCreate: Текущая дата: $selectedDate")

        // Обработчик выбора даты
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            selectedDateText.text = "Выбранная дата: $selectedDate"
            Log.d("MainActivity", "onDateChange: Выбранная дата изменена на $selectedDate")
            updateEventList()
        }

        // Обработчик добавления события
        addEventButton.setOnClickListener {
            Log.d("MainActivity", "onAddEvent: Кнопка добавления события нажата")
            showAddEventDialog()
        }

        // Обновление списка событий для текущей даты
        updateEventList()
        Log.d("MainActivity", "onCreate: Инициализация завершена")
    }

    private fun addEvent(event: String) {
        if (!eventsMap.containsKey(selectedDate)) {
            eventsMap[selectedDate] = mutableListOf()
        }
        eventsMap[selectedDate]?.add(event)
        saveEvents()
        updateEventList()
        Log.d("MainActivity", "addEvent: Добавлено событие '$event' для даты $selectedDate")
    }

    private fun updateEventList() {
        val events = eventsMap[selectedDate] ?: emptyList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, events)
        eventListView.adapter = adapter
        Log.d("MainActivity", "updateEventList: Обновлен список событий для $selectedDate: $events")
    }

    private fun showAddEventDialog() {
        val builder = AlertDialog.Builder(this)
        val input = EditText(this)
        builder.setTitle("Добавить событие")
        builder.setView(input)

        builder.setPositiveButton("Добавить") { dialog, _ ->
            val event = input.text.toString()
            if (event.isNotEmpty()) {
                addEvent(event)
            } else {
                Log.w("MainActivity", "showAddEventDialog: Попытка добавить пустое событие")
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Отмена") { dialog, _ ->
            Log.d("MainActivity", "showAddEventDialog: Добавление события отменено")
            dialog.cancel()
        }

        builder.show()
    }

    private fun saveEvents() {
        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        try {
            for ((key, value) in eventsMap) {
                editor.putString(key, value.joinToString(";"))
            }
            editor.apply()
            Log.d("MainActivity", "saveEvents: События сохранены: $eventsMap")
        } catch (e: Exception) {
            Log.e("MainActivity", "saveEvents: Ошибка при сохранении событий", e)
        }
    }

    private fun loadEvents() {
        val sharedPreferences = getSharedPreferences(sharedPreferencesKey, Context.MODE_PRIVATE)
        sharedPreferences.all.forEach { (key, value) ->
            if (value is String) {
                eventsMap[key] = value.split(";").toMutableList()
            }
        }
        Log.d("MainActivity", "loadEvents: Загружены события: $eventsMap")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        Log.d("MainActivity", "onCreateOptionsMenu: Меню создано")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.aboutMenu -> {
                Log.d("MainActivity", "onOptionsItemSelected: Пункт меню 'О программе' выбран")
                showAboutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("О программе")
            .setMessage("Приложение для записи событий в календаре. Версия 1.0.")
            .setPositiveButton("OK", null)
            .show()
        Log.d("MainActivity", "showAboutDialog: О программе показано")
    }

    companion object {
        private fun getCurrentDate(): String {
            val calendar = Calendar.getInstance()
            val currentDate = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
            Log.d("MainActivity", "getCurrentDate: Текущая дата: $currentDate")
            return currentDate
        }
    }
}