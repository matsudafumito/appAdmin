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

class AdminUserAccountSearch : AppCompatActivity() {

    companion object{
        const val getUserInfoId: Int = 4
        var token = ""
    }

    private val uri = WsClient.serverRemote
    private var client = GetUserInfoWsClient(this, uri)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_account_search)
        client.connect()

        //view
        val buttonHome : Button = findViewById(R.id.buttonHome)
        buttonHome.setOnClickListener {
            val intent = Intent(this,AdminRegisterAccount::class.java)
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
            val searchUser: String = searchtext.query.toString()
            val getInfoParams = JSONObject()
            getInfoParams.put("searchBy", "user_name")
            getInfoParams.put("user_name", searchUser)
            getInfoParams.put("token", token)
            val getInfoRequest = client.createJsonrpcReq("getInfo/user/basic", getUserInfoId, getInfoParams)
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

class GetUserInfoWsClient(private val activity: Activity, uri: URI) : WsClient(uri){


    override fun onMessage(message: String?) {
        super.onMessage(message)
        Log.i(javaClass.simpleName, "msg arrived")
        Log.i(javaClass.simpleName, "$message")

        val wholeMsg = JSONObject("$message")
        val resId: Int = wholeMsg.getInt("id")
        val result: JSONObject = wholeMsg.getJSONObject("result")
        val status: String = result.getString("status")

        if(resId == AdminUserAccountSearch.getUserInfoId){
            if(status == "success"){
                val intent = Intent(activity, AdminUserAccountManagement::class.java) //画面遷移
                intent.putExtra("userId", result.getInt("user_id"))
                intent.putExtra("userName", result.getString("user_name"))
                intent.putExtra("birthday", result.getString("birthday"))
                intent.putExtra("gender", result.getString("gender"))
                intent.putExtra("emailAddr", result.getString("email_addr"))
                intent.putExtra("address", result.getString("address"))
                intent.putExtra("num_vicious_cancels" , result.getInt("num_vicious_cancels"))
                intent.putExtra("token", AdminUserAccountSearch.token)
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