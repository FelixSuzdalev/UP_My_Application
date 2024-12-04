package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var addEventButton: Button
    private lateinit var selectedDateText: TextView
    private lateinit var eventListView: ListView

    // Хранилище событий в виде Map
    private val eventsMap = mutableMapOf<String, MutableList<String>>()

    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация элементов
        calendarView = findViewById(R.id.calendarView)
        addEventButton = findViewById(R.id.addEventButton)
        selectedDateText = findViewById(R.id.selectedDate)
        eventListView = findViewById(R.id.eventListView)

        // Установка текущей даты
        selectedDate = getCurrentDate()
        selectedDateText.text = "Выбранная дата: $selectedDate"

        // Обработчик выбора даты
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            selectedDateText.text = "Выбранная дата: $selectedDate"
            updateEventList()
        }

        // Обработчик добавления события
        addEventButton.setOnClickListener {
            showAddEventDialog()
        }

        // Обновление списка событий для текущей даты
        updateEventList()
    }

    // Получить текущую дату
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    // Диалог для добавления события
    private fun showAddEventDialog() {
        val input = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Новое событие")
            .setMessage("Введите описание события:")
            .setView(input)
            .setPositiveButton("Добавить") { _, _ ->
                val eventText = input.text.toString()
                if (eventText.isNotEmpty()) {
                    addEvent(eventText)
                } else {
                    Toast.makeText(this, "Событие не может быть пустым", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .create()
        dialog.show()
    }

    // Добавить событие в список
    private fun addEvent(event: String) {
        if (!eventsMap.containsKey(selectedDate)) {
            eventsMap[selectedDate] = mutableListOf()
        }
        eventsMap[selectedDate]?.add(event)
        updateEventList()
    }

    // Обновить список событий для выбранной даты
    private fun updateEventList() {
        val events = eventsMap[selectedDate] ?: mutableListOf()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, events)
        eventListView.adapter = adapter
    }
}