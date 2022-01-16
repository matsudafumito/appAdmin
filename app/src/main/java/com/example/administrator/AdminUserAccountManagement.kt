package com.example.administrator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class AdminUserAccountManagement : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_account_management)
        //view
        val buttonHome : Button = findViewById(R.id.buttonHome)
        buttonHome.setOnClickListener {
            val intent = Intent(this,Administrator::class.java)
            startActivity(intent)
        }

        val Ban : Button = findViewById(R.id.Ban)

        val multiAutoCompleteTextView : TextView = findViewById(R.id.multiAutoCompleteTextView)
    }

}