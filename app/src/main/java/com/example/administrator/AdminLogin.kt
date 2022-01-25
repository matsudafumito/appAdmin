package com.example.administrator

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.net.URI
import org.json.JSONObject

class AdminLogin : AppCompatActivity() {
    companion object{
        const val loginReqId: Int = 1
    }

    private val uri = WsClient.serverRemote
    private var client = LoginWsClient(this, uri)

    override fun onRestart() {
        super.onRestart()
        client = LoginWsClient(this, uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        client.connect()
        //view
        val textBoxUserName : EditText = findViewById(R.id.textBoxUserName)

        val textBoxPassword : EditText = findViewById(R.id.textBoxPassword)

        val buttonLogin : Button = findViewById(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            val loginParams = JSONObject()
            val userName: String = textBoxUserName.text.toString()
            val password: String = textBoxPassword.text.toString()
            loginParams.put("user_name", userName)
            loginParams.put("password", password)
            loginParams.put("role", "admin")
            val loginRequest = client.createJsonrpcReq("login", loginReqId, loginParams)

            try{
                if(client.isClosed) {
                    client.reconnect()
                }
                client.send(loginRequest.toString())
            } catch (ex: Exception){
                Log.i(javaClass.simpleName, "send failed $ex")
            }
        }

        val buttonAccount : Button = findViewById(R.id.buttonAccount)
        buttonAccount.setOnClickListener {
            val intent = Intent(this, AdminRegisterAccount::class.java)
            startActivity(intent)
        }

    }

}

class LoginWsClient(private val activity: Activity, uri: URI) : WsClient(uri){
    override fun onMessage(message: String?) {
        super.onMessage(message)
        Log.i(javaClass.simpleName, "msg arrived")
        Log.i(javaClass.simpleName, "$message")
        val wholeMsg = JSONObject("$message")
        val resId: Int = wholeMsg.getInt("id")
        val result: JSONObject = wholeMsg.getJSONObject("result")
        val status: String = result.getString("status")
        //if message is about login
        if(resId == AdminLogin.loginReqId){
            if(status == "success"){
                val token: String = result.getString("token")
                val expire: String = result.getString("expire")
                Log.i(javaClass.simpleName, "login success")
                Log.i(javaClass.simpleName, "token: $token")
                Log.i(javaClass.simpleName, "expires in $expire")

                Administrator.globalToken = token

                this.close(NORMAL_CLOSURE)
                activity.runOnUiThread{
                    val intent = Intent(activity, Administrator::class.java)
                    intent.putExtra("token", token)
                    intent.putExtra("expire", expire)
                    activity.startActivity(intent)
                }

            }else if(status == "error"){
                activity.runOnUiThread{
                    val reason: String = result.getString("reason")
                    Log.i(javaClass.simpleName, "login failed with reason $reason")
                }
            }
        }
    }
}