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

class AdminWithdrawal : AppCompatActivity() {

    companion object{
        const val adminResignReqId = 6
    }

    private val uri = WsClient.serverRemote
    private val client = AdminResignWsClient(this, uri)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_withdrawal)

        val buttonHome : Button = findViewById(R.id.buttonHome)
        buttonHome.setOnClickListener {
            val intent = Intent(this,Administrator::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        client.connect()
        val token = Administrator.globalToken
        val etxtPassoword:EditText = findViewById(R.id.textBoxPassword)
        val buttonResign: Button = findViewById(R.id.buttonSubmit)

        buttonResign.setOnClickListener {
            val resignParams = JSONObject()
            resignParams.put("token", token)
            resignParams.put("password", etxtPassoword.text.toString())
            val resignReq = client.createJsonrpcReq("resign", adminResignReqId, resignParams)

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

    override fun onBackPressed() {
        super.onBackPressed()
        client.close(WsClient.NORMAL_CLOSURE)
    }
}


class AdminResignWsClient(private val activity: Activity, uri: URI) : WsClient(uri){

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

        if (resId == AdminWithdrawal.adminResignReqId){
            if(status == "success"){
                val intent = Intent(activity, ShowResult::class.java)
                intent.putExtra("message", "アカウント削除が完了しました")
                intent.putExtra("transitionBtnMessage", "トップへ")
                intent.putExtra("isBeforeLogin", true)
                activity.startActivity(intent)
                this.close(NORMAL_CLOSURE)

            }else if(status == "error"){
                activity.runOnUiThread {
                    errorDisplay.text = "パスワードが間違っています"
                    errorDisplay.visibility = View.VISIBLE
                }
            }
        }
    }
}