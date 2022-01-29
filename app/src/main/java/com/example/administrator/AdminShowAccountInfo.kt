package com.example.administrator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.*

class AdminShowAccountInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_show_account_info)

        val buttonHome : Button = findViewById(R.id.buttonHome)
        buttonHome.setOnClickListener {
            val intent = Intent(this,Administrator::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        val errorDisplay: TextView = findViewById(R.id.errorDisplay)
        val buttonChangeAccountInfo: Button = findViewById(R.id.buttonChangeAccountInfo)

        val token = Administrator.globalToken
        val userName = Administrator.globalUserName
        val userId = Administrator.globalAdminId
        val birthday = intent.getStringExtra("birthday")
        val gender = intent.getStringExtra("gender")
        val email = intent.getStringExtra("email")
        val address = intent.getStringExtra("address")

        val txtUserName: TextView = findViewById(R.id.textBoxUserName)
        val txtBirthday: TextView = findViewById(R.id.textBoxUserBirthday)
        val txtGender: TextView = findViewById(R.id.textBoxUserGender)
        val txtEmail: TextView = findViewById(R.id.textBoxUserEmail)
        val txtAddress: TextView = findViewById(R.id.textBoxUserAddress)

        txtUserName.text = userName
        txtBirthday.text = birthday
        txtGender.text = gender
        txtEmail.text = email
        txtAddress.text = address

        buttonChangeAccountInfo.setOnClickListener {
            val intent = Intent(this@AdminShowAccountInfo, AdminAccountInfoChange::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("userName", userName)
            intent.putExtra("birthday", birthday)
            intent.putExtra("gender", gender)
            intent.putExtra("emailAddr", email)
            intent.putExtra("address", address)
            intent.putExtra("token", token)
            startActivity(intent)
        }
    }

}