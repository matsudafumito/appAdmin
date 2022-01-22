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
import java.net.URI

class AdminUserAccountManagement : AppCompatActivity() {
    companion object{
        const val getUserInfoId: Int = 4
    }

    private val uri = WsClient.serverRemote
    private var client = GetUserInfo(this, uri)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_user_account_management)
        //view
        val buttonHome : Button = findViewById(R.id.buttonHome)
        buttonHome.setOnClickListener {
            val intent = Intent(this,Administrator::class.java)
            startActivity(intent)
        }

        val Ban : Button = findViewById(R.id.Ban)

    }

}
class GetUserInfo(private val activity: Activity, uri: URI) : WsClient(uri){
    var userId: Int = -1
    var userName = ""
    var birthday = ""
    var gender = ""
    var emailAddr = ""
    var address = ""
    var numViciousCancels = ""
    var isReceived = false

    //private val errorDisplay: TextView by lazy { activity.findViewById(R.id.errorDisplay) }
    private val txtUserName: TextView by lazy { activity.findViewById(R.id.textBoxUserName) }
    private val txtBirthDay: TextView by lazy { activity.findViewById(R.id.textBoxUserBirthday) }
    private val txtGender: TextView by lazy { activity.findViewById(R.id.textBoxUserGender) }
    private val txtEmail: TextView by lazy { activity.findViewById(R.id.textBoxUserEmail) }
    private val txtAddress: TextView by lazy { activity.findViewById(R.id.textBoxUserAddress) }
    private val txtNumViciousCancels: TextView by lazy { activity.findViewById(R.id.txtNumViciousCancels) }

    override fun onMessage(message: String?) {
        super.onMessage(message)
        Log.i(javaClass.simpleName, "msg arrived")
        Log.i(javaClass.simpleName, "$message")

        val wholeMsg = JSONObject("$message")
        val resId: Int = wholeMsg.getInt("id")
        val result: JSONObject = wholeMsg.getJSONObject("result")
        val status: String = result.getString("status")

        if(resId == AdminUserAccountManagement.getUserInfoId){
            this.isReceived = true
            if(status == "success"){
                this.userId = result.getInt("user_id")
                this.userName = result.getString("user_name")
                this.birthday = result.getString("birthday")
                this.gender = result.getString("gender")
                this.emailAddr = result.getString("email_addr")
                this.address = result.getString("address")
                this.numViciousCancels = result.getString("num_vicious_cancels")

                activity.runOnUiThread{
                    txtUserName.text = this.userName
                    txtBirthDay.text = this.birthday
                    txtGender.text = this.gender
                    txtEmail.text = this.emailAddr
                    txtAddress.text = this.address
                    txtNumViciousCancels.text = this.numViciousCancels
                }
            }else if(status == "error"){
                activity.runOnUiThread{
                    //errorDisplay.text = "アカウント情報を取得できません"
                    //errorDisplay.visibility = View.INVISIBLE
                }
            }
        }
    }
}