package com.example.administrator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AdminRegisterAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_register_account)

        //view
        val textBoxUserName : EditText = findViewById(R.id.textBoxUserName)

        val textBoxPassword : EditText = findViewById(R.id.textBoxPassword)

        val textBoxAdminPassword : EditText = findViewById(R.id.textBoxAdminPassword)

        val buttonRegister : Button = findViewById(R.id.buttonRegister)
        buttonRegister.setOnClickListener {
            val intent = Intent(this,Administrator::class.java)
            startActivity(intent)
        }
    }
}