package com.example.administrator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AdminUserAccountManagement : AppCompatActivity() {
    companion object{
        const val getUserInfoId: Int = 5
    }

    private val uri = WsClient.serverRemote

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_account_management)
        //view



        val buttonHome : Button = findViewById(R.id.buttonHome)
        buttonHome.setOnClickListener {
            val intent = Intent(this,Administrator::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        val Ban : Button = findViewById(R.id.Ban)
        val currentUserName = intent.getStringExtra("userName")
        val currentBirthday = intent.getStringExtra("birthday")
        val currentGender = intent.getStringExtra("gender")
        val currentEmail = intent.getStringExtra("emailAddr")
        val currentAddress = intent.getStringExtra("address")
        val currentCancel = intent.getIntExtra("num_vicious_cancels",-1)

        val etxtUserName: EditText = findViewById(R.id.textBoxUserName)
        val etxtUserBirthday: EditText = findViewById(R.id.textBoxUserBirthday)
        val etxtUserGender: EditText = findViewById(R.id.textBoxUserGender)
        val etxtUserEmail: EditText = findViewById(R.id.textBoxUserEmail)
        val etxtUserAddress: EditText = findViewById(R.id.textBoxUserAddress)
        val etxtUserCancel: EditText = findViewById(R.id.txtNumViciousCancels)

        etxtUserName.setText(currentUserName)
        etxtUserBirthday.setText(currentBirthday)
        etxtUserGender.setText(currentGender)
        etxtUserEmail.setText(currentEmail)
        etxtUserAddress.setText(currentAddress)
        etxtUserCancel.setText(currentCancel)

        Ban.setOnClickListener {

        }

    }

}
