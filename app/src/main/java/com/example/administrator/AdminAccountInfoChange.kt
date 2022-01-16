package com.example.administrator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AdminAccountInfoChange : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //view
        val edit_text : EditText = findViewById(R.id.edit_text)

        val edit_text2 : EditText = findViewById(R.id.edit_text2)

        val edit_text3 : EditText = findViewById(R.id.edit_text3)

        val edit_text4 : EditText = findViewById(R.id.edit_text4)

        val edit_text5 : EditText = findViewById(R.id.edit_text5)

        val backInfo : Button = findViewById(R.id.backInfo)
        backInfo.setOnClickListener {
            val intent = Intent(this,Administrator::class.java)
            startActivity(intent)
        }

        val nextCheck : Button = findViewById(R.id.nextCheck)
        nextCheck.setOnClickListener {
            val intent = Intent(this,AdminInfoCheck::class.java)
            startActivity(intent)
        }
    }
}