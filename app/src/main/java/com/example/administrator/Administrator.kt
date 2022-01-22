package com.example.administrator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class Administrator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrator)

        //view
        val buttonHome : Button = findViewById(R.id.buttonHome)
        buttonHome.setOnClickListener {
            val intent = Intent(this,Administrator::class.java)
            startActivity(intent)
        }

        val buttonSearchRestaurant : Button = findViewById(R.id.buttonSearchRestaurant)
        buttonSearchRestaurant.setOnClickListener {
            val intent = Intent(this,AdminRestaurantAccountSearch::class.java)
            startActivity(intent)
        }

        val buttonSearchUser : Button = findViewById(R.id.buttonSearchUser)
        buttonSearchUser.setOnClickListener {
            val token = intent.getStringExtra("token")
            Log.i(javaClass.simpleName, "token by getStringExtra: $token")
            val intent = Intent(this,AdminUserAccountSearch::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        val buttonSetting : Button = findViewById(R.id.buttonSetting)
        buttonSetting.setOnClickListener {
            val intent = Intent(this,AdminAccountInfoChange::class.java)
            startActivity(intent)
        }
    }
}