package com.example.administrator

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import org.json.JSONObject
import java.net.URI
import java.util.*
import kotlin.concurrent.schedule

class AdminRestaurantAccountSearch : AppCompatActivity() {

    companion object{
        const val getRestaurantInfoId: Int = 5
        var token = ""
    }

    private val uri = WsClient.serverRemote
    private var client = GetRestaurantInfoWsClient(this, uri)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_restaurant_account_search)
        client.connect()

        //view
        val buttonHome : Button = findViewById(R.id.buttonHome)
        buttonHome.setOnClickListener {
            val intent = Intent(this,Administrator::class.java)
            startActivity(intent)
        }
    }
    //change
    override fun onResume() {
        super.onResume()
        //val errorDisplay: TextView = findViewById(R.id.errorDisplay)
        val buttonSearch: Button = findViewById(R.id.buttonSearch)
        val searchtext : SearchView = findViewById(R.id.searchtext)

        val exToken = intent.getStringExtra("token")
        token = exToken!!
        Log.i(javaClass.simpleName, "token by getStringExtra: $token")


        buttonSearch.setOnClickListener {
            val searchRestaurant: String = searchtext.query.toString()
            val getInfoParams = JSONObject()
            getInfoParams.put("searchBy", "restaurant_name")
            getInfoParams.put("restaurant_name", searchRestaurant)
            getInfoParams.put("token", token)
            val getInfoRequest = client.createJsonrpcReq("getInfo/restaurant/basic", getRestaurantInfoId, getInfoParams)
            try {
                if (client.isClosed) {
                    client.reconnect()
                }
                client.send(getInfoRequest.toString())
                //errorDisplay.text = "情報取得中..."
                //errorDisplay.visibility = View.VISIBLE
            } catch (ex: Exception) {
                Log.i(javaClass.simpleName, "send failed")
                Log.i(javaClass.simpleName, "$ex")
            }
        }
    }
}

class GetRestaurantInfoWsClient(private val activity: Activity, uri: URI) : WsClient(uri){


    override fun onMessage(message: String?) {
        super.onMessage(message)
        Log.i(javaClass.simpleName, "msg arrived")
        Log.i(javaClass.simpleName, "$message")

        val wholeMsg = JSONObject("$message")
        val resId: Int = wholeMsg.getInt("id")
        val result: JSONObject = wholeMsg.getJSONObject("result")
        val status: String = result.getString("status")

        if(resId == AdminRestaurantAccountSearch.getRestaurantInfoId){
            if(status == "success"){
                val intent = Intent(activity, AdminRestaurantAccountManagement::class.java) //画面遷移
                intent.putExtra("restaurantId", result.getInt("restaurant_id"))
                intent.putExtra("restaurantName", result.getString("restaurant_name"))
                intent.putExtra("timeOpen", result.getString("time_open"))
                intent.putExtra("timeClose", result.getString("time_close"))
                intent.putExtra("holidays", result.getString("holidays"))
                intent.putExtra("emailAddr", result.getString("email_addr"))
                intent.putExtra("address" , result.getString("address"))
                intent.putExtra("token", AdminRestaurantAccountSearch.token)
                activity.startActivity(intent)
                this.close(WsClient.NORMAL_CLOSURE)

            }else if(status == "error"){
                Log.i(javaClass.simpleName, "no user matched")
                activity.runOnUiThread{
                    //errorDisplay.text = "アカウント情報を取得できません"
                    //errorDisplay.visibility = View.INVISIBLE
                }
            }
        }
    }
}