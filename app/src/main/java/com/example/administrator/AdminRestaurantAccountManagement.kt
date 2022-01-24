package com.example.administrator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class AdminRestaurantAccountManagement : AppCompatActivity() {
    companion object{
        const val getRestaurantInfoId: Int = 5
    }

    private val uri = WsClient.serverRemote

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_restaurant_account_management)
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
        val currentRestaurantName = intent.getStringExtra("restaurantName")
        val currentTimeOpen = intent.getStringExtra("timeOpen")
        val currentTimeClose = intent.getStringExtra("timeClose")
        val currentHolidays = intent.getStringExtra("holidays")
        val currentRestaurantEmail = intent.getStringExtra("emailAddr")
        val currentRestaurantAddress = intent.getStringExtra("address")

        val etxtRestaurantName: EditText = findViewById(R.id.textBoxRestaurantName)
        val etxtTimeOpen: EditText = findViewById(R.id.textBoxTimeOpen)
        val etxtTimeClose: EditText = findViewById(R.id.textBoxTimeClose)
        val etxtHolidays: EditText = findViewById(R.id.textBoxHolidays)
        val etxtRestaurantEmail: EditText = findViewById(R.id.textBoxRestaurantEmail)
        val etxtRestaurantAddress: EditText = findViewById(R.id.textBoxRestaurantAddress)

        etxtRestaurantName.setText(currentRestaurantName)
        etxtTimeOpen.setText(currentTimeOpen)
        etxtTimeClose.setText(currentTimeClose)
        etxtHolidays.setText(currentHolidays)
        etxtRestaurantEmail.setText(currentRestaurantEmail)
        etxtRestaurantAddress.setText(currentRestaurantAddress)

        Ban.setOnClickListener {

        }

    }

}