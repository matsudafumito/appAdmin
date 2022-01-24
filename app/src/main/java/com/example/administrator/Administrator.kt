package com.example.administrator

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONObject
import java.net.URI

class Administrator : AppCompatActivity() {
    private var user_name: String = ""

    companion object {
        const val logoutReqId: Int = 3
        const val getAdminInfoId: Int = 7
        var globalToken = ""
        var globalAdminId = -1
        var globalUserName = ""
        var globalTokenExpiry = ""
    }

    private val uri = WsClient.serverRemote
    private var client = AdminTopWsClient(this, uri)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrator)
    }

    override fun onResume() {
        super.onResume()
        client.connect()

        val getUserInfo = Runnable {
            while (!client.isOpen){
                //do nothing
                //wait until websocket open
            }
            //the connection openings is guaranteed -> attach no error handler
            client.sendReqGetUserInfoByName(globalToken, this.user_name)
            return@Runnable
        }
        Thread( getUserInfo ).start()

        Log.i(javaClass.simpleName, "token recved $globalToken")
        Log.i(javaClass.simpleName, "token expiry $globalTokenExpiry")
        Log.i(javaClass.simpleName, "userName: $globalUserName")

        val buttonToHome: Button = findViewById(R.id.buttonHome)
        val buttonToSearchRestaurant: Button = findViewById(R.id.buttonSearchRestaurant)
        val buttonToSetting: Button = findViewById(R.id.buttonSetting)
        val buttonSearchUser: Button = findViewById(R.id.buttonSearchUser)
        val buttonLogout: Button = findViewById(R.id.buttonLogout)

        buttonSearchUser.setOnClickListener {
            val intent = Intent(this@Administrator, AdminUserAccountSearch::class.java)
            intent.putExtra("userName", globalUserName)
            intent.putExtra("token", globalToken)
            client.close(WsClient.NORMAL_CLOSURE)
            startActivity(intent)
        }

        buttonToHome.setOnClickListener {
            //doNothing
        }

        buttonToSearchRestaurant.setOnClickListener {
            val intent = Intent(this@Administrator, AdminRestaurantAccountSearch::class.java)
            intent.putExtra("userName", globalUserName)
            intent.putExtra("token", globalToken)
            client.close(WsClient.NORMAL_CLOSURE)
            startActivity(intent)
        }

        buttonToSetting.setOnClickListener {
            val intent = Intent(this@Administrator, AdminAccountInfoChange::class.java)
            intent.putExtra("userName", globalUserName)
            intent.putExtra("token", globalToken)
            client.close(WsClient.NORMAL_CLOSURE)
            startActivity(intent)
        }

        buttonLogout.setOnClickListener {
            val logoutParams = JSONObject()
            logoutParams.put("token", globalToken)
            val logoutRequest = client.createJsonrpcReq("logout", logoutReqId, logoutParams)

            try{
                if(client.isClosed) {
                    client.reconnect()
                }
                client.send(logoutRequest.toString())
            } catch (ex: Exception){
                Log.i(javaClass.simpleName, "send failed $ex")
                val intent = Intent(this@Administrator, ShowResult::class.java)
                val message = "ログアウトしました"
                val transitionBtnMessage = "ログインページへ"
                val isBeforeLogin = true
                Log.i(javaClass.simpleName, "logout with no request")
                intent.putExtra("message", message)
                intent.putExtra("transitionBtnMessage", transitionBtnMessage)
                intent.putExtra("isBeforeLogin", isBeforeLogin)
                startActivity(intent)
                finish()
            }
        }

    }

}
class AdminTopWsClient(private val activity: Activity, uri: URI) : WsClient(uri){

    var user_id: Int = -1
    var user_name: String = ""
    var birthday: String = ""
    var gender: String = ""
    var email_addr: String = ""
    var address: String = ""

    fun isUserInfoArrived(): Boolean{
        if(this.user_id == -1){
            return false
        }
        return true
    }

    /**
     * this method will send request about getting user information
     */
    fun sendReqGetUserInfoByName(token: String, clientName: String){
        Log.i(javaClass.simpleName, "send request to get user information")
        val params = JSONObject()
        params.put("searchBy", "user_name")
        params.put("user_name", Administrator.globalUserName)
        params.put("token", Administrator.globalToken)
        val request = this.createJsonrpcReq("getInfo/user/basic", Administrator.getAdminInfoId, params)
        this.send(request.toString())
    }

    override fun onOpen(handshakedata: ServerHandshake?) {
        super.onOpen(handshakedata)
        this.sendReqGetUserInfoByName(Administrator.globalToken, Administrator.globalUserName)
    }

    override fun onMessage(message: String?) {
        super.onMessage(message)
        Log.i(javaClass.simpleName, "msg arrived")
        Log.i(javaClass.simpleName, "$message")

        val wholeMsg = JSONObject("$message")
        val resId: Int = wholeMsg.getInt("id")
        val result: JSONObject = wholeMsg.getJSONObject("result")
        val status: String = result.getString("status")

        //if message is about logout
        if(resId == Administrator.logoutReqId){

            val intent = Intent(activity, ShowResult::class.java)
            var message = ""
            val transitionBtnMessage = "ログインページへ"
            val isBeforeLogin = true

            //if logout successes
            if(status == "success"){
                message = "ログアウトに成功しました"

            }else if(status == "error"){
                message = "ログアウトに失敗しました"
            }

            intent.putExtra("message", message)
            intent.putExtra("transitionBtnMessage", transitionBtnMessage)
            intent.putExtra("isBeforeLogin", isBeforeLogin)

            activity.startActivity(intent)
            activity.finish()
            this.close(NORMAL_CLOSURE)

            //if msg is about getInfo/user/basic
        }else if(resId == Administrator.getAdminInfoId){
            if(status == "success"){
                this.user_id = result.getInt("user_id")
                this.user_name = result.getString("user_name")
                this.birthday = result.getString("birthday")
                this.gender = result.getString("gender")
                this.email_addr = result.getString("email_addr")
                this.address = result.getString("address")
                Administrator.globalAdminId = result.getInt("user_id")

            }else if(status == "error"){
                Log.i(javaClass.simpleName, "getInfo failed")
            }
        }
    }
}
