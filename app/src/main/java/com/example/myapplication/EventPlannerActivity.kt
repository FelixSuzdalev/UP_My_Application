package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.*

class EventPlannerActivity : AppCompatActivity() {

    private lateinit var eventList: MutableList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_planner)

        val listView = findViewById<ListView>(R.id.listView)
        val editTextEvent = findViewById<EditText>(R.id.editTextEvent)
        val buttonAdd = findViewById<Button>(R.id.buttonAdd)

        eventList = loadEvents()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, eventList)
        listView.adapter = adapter

        buttonAdd.setOnClickListener {
            val event = editTextEvent.text.toString()
            if (event.isNotEmpty()) {
                eventList.add(event)
                adapter.notifyDataSetChanged()
                saveEvents()
                editTextEvent.text.clear()
            } else {
                Toast.makeText(this, "Введите мероприятие", Toast.LENGTH_SHORT).show()
            }
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            val event = eventList[position]
            eventList.removeAt(position)
            adapter.notifyDataSetChanged()
            saveEvents()
            Toast.makeText(this, "Мероприятие \"$event\" удалено", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun saveEvents() {
        try {
            val fileOutputStream = openFileOutput("events.txt", Context.MODE_PRIVATE)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            for (event in eventList) {
                outputStreamWriter.write("$event\n")
            }
            outputStreamWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadEvents(): MutableList<String> {
        val events = mutableListOf<String>()
        try {
            val fileInputStream = openFileInput("events.txt")
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var line = bufferedReader.readLine()
            while (line != null) {
                events.add(line)
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
        } catch (e: FileNotFoundException) {
            // Файл не найден, возвращаем пустой список
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return events
    }


    }
