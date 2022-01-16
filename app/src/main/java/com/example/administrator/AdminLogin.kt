package com.example.administrator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class AdminLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        //view
        val textBoxUserName : EditText = findViewById(R.id.textBoxUserName)

        val textBoxPassword : EditText = findViewById(R.id.textBoxPassword)

        val buttonLogin : Button = findViewById(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            val intent = Intent(this, Administrator::class.java)
            startActivity(intent)
        }

        val buttonAccount : Button = findViewById(R.id.buttonAccount)
        buttonAccount.setOnClickListener {
            val intent = Intent(this, AdminRegisterAccount::class.java)
            startActivity(intent)
        }

    }

}