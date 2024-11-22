package com.example.crypto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SQLiteActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editInfo: EditText
    private lateinit var textViewData: TextView
    private lateinit var buttonSave: Button
    private lateinit var buttonGoToMain: Button

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqlite)

        // Initialize UI components
        editName = findViewById(R.id.editName)
        editInfo = findViewById(R.id.editInfo)
        textViewData = findViewById(R.id.textViewData)
        buttonSave = findViewById(R.id.buttonSave)
        buttonGoToMain = findViewById(R.id.buttonGoToMain)

        // Initialize Database Helper
        databaseHelper = DatabaseHelper(this)

        // Save button action
        buttonSave.setOnClickListener {
            val name = editName.text.toString()
            val info = editInfo.text.toString()
            databaseHelper.insertUser(name, info)
            updateData()
        }

        // Go to Main Activity button action
        buttonGoToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Load existing data on start
        updateData()
    }

    private fun updateData() {
        val data = databaseHelper.getAllUsers()
        textViewData.text = if (data.isNotEmpty()) data else "No data available"
    }
}
