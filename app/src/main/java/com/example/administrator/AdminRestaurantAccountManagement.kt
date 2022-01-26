package com.example.administrator

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.json.JSONObject
import java.lang.Exception
import java.net.URI

class AdminRestaurantAccountManagement : AppCompatActivity() {
    companion object{
        const val restaurantResignReqId: Int = 5
    }

    private val uri = WsClient.serverRemote
    private val client = RestaurantResignWsClient(this, uri)

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

        val currentRestaurantId = intent.getIntExtra("restaurantId", 0)
        val currentRestaurantName = intent.getStringExtra("restaurantName")
        val currentTimeOpen = intent.getStringExtra("timeOpen")
        val currentTimeClose = intent.getStringExtra("timeClose")
        val currentHolidays = intent.getStringExtra("holidays")
        val currentRestaurantEmail = intent.getStringExtra("emailAddr")
        val currentRestaurantAddress = intent.getStringExtra("address")

        val token = Administrator.globalToken
        var userName = Administrator.globalUserName

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
            val resignParams = JSONObject()
            resignParams.put("token", token)
            resignParams.put("account_role", "restaurant")
            resignParams.put("account_id", currentRestaurantId)
            val resignReq = client.createJsonrpcReq("resign/forced",
                AdminRestaurantAccountManagement.restaurantResignReqId, resignParams)

            try {
                if (client.isClosed) {
                    client.reconnect()
                }
                client.send(resignReq.toString())
            } catch (ex: Exception) {
                Log.i(javaClass.simpleName, "send failed")
                Log.i(javaClass.simpleName, "$ex")
                //errorDisplay.text = "インターネットに接続されていません"
                //errorDisplay.visibility = View.VISIBLE
            }
        }

    }

}
class RestaurantResignWsClient(private val activity: Activity, uri: URI) : WsClient(uri){

    private val errorDisplay: TextView by lazy {
        activity.findViewById(R.id.errorDisplay)
    }

    override fun onMessage(message: String?) {
        super.onMessage(message)
        Log.i(javaClass.simpleName, "msg arrived")
        Log.i(javaClass.simpleName, "$message")

        val wholeMsg = JSONObject("$message")
        val resId: Int = wholeMsg.getInt("id")
        val result: JSONObject = wholeMsg.getJSONObject("result")
        val status: String = result.getString("status")

        if (resId == AdminRestaurantAccountManagement.restaurantResignReqId){
            if(status == "success"){
                activity.runOnUiThread {
                    val intent = Intent(activity, ShowResult::class.java)
                    intent.putExtra("message", "アカウント削除が完了しました")
                    intent.putExtra("transitionBtnMessage", "トップへ")
                    intent.putExtra("isBeforeLogin", false)
                    activity.startActivity(intent)
                    this.close(NORMAL_CLOSURE)
                }

            }else if(status == "error"){
                activity.runOnUiThread {
                    errorDisplay.text = "パスワードが間違っています"
                    errorDisplay.visibility = View.VISIBLE
                }
            }
        }
    }
}