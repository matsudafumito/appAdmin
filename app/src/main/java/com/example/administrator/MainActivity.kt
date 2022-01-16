package com.example.administrator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //view
        val nextregister : Button = findViewById(R.id.nextregister)
        nextregister.setOnClickListener {
            val intent = Intent(this,AdminRegisterAccount::class.java)
            startActivity(intent)
        }
        val nextlogin : Button = findViewById(R.id.nextlogin)
        nextlogin.setOnClickListener {
            val intent = Intent(this,AdminLogin::class.java)
            startActivity(intent)
        }
    }
}