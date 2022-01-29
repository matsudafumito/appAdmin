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

class AdminUserAccountManagement : AppCompatActivity() {
    companion object{
        const val userResignReqId = 6
    }

    private val uri = WsClient.serverRemote
    private val client = UserResignWsClient(this, uri)

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
        client.connect()
        val Ban : Button = findViewById(R.id.Ban)

        val currentUserId = intent.getIntExtra("userId", 0)
        val currentUserName = intent.getStringExtra("userName")
        val currentBirthday = intent.getStringExtra("birthday")
        val currentGender = intent.getStringExtra("gender")
        val currentEmail = intent.getStringExtra("emailAddr")
        val currentAddress = intent.getStringExtra("address")
        val currentCancel = intent.getIntExtra("num_vicious_cancels",-1)

        val token = Administrator.globalToken
        var userName = Administrator.globalUserName

        val etxtUserName: TextView = findViewById(R.id.textBoxUserName)
        val etxtUserBirthday: TextView = findViewById(R.id.textBoxUserBirthday)
        val etxtUserGender: TextView = findViewById(R.id.textBoxUserGender)
        val etxtUserEmail: TextView = findViewById(R.id.textBoxUserEmail)
        val etxtUserAddress: TextView = findViewById(R.id.textBoxUserAddress)
        val etxtUserCancel: TextView = findViewById(R.id.txtNumViciousCancels)

        etxtUserName.setText(currentUserName)
        etxtUserBirthday.setText(currentBirthday)
        etxtUserGender.setText(currentGender)
        etxtUserEmail.setText(currentEmail)
        etxtUserAddress.setText(currentAddress)
        etxtUserCancel.setText(currentCancel.toString())

        Ban.setOnClickListener {
            val resignParams = JSONObject()
            resignParams.put("token", token)
            resignParams.put("account_role", "user")
            resignParams.put("account_id", currentUserId)
            val resignReq = client.createJsonrpcReq("resign/forced",userResignReqId, resignParams)

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
class UserResignWsClient(private val activity: Activity, uri: URI) : WsClient(uri){

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

        if (resId == AdminUserAccountManagement.userResignReqId){
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