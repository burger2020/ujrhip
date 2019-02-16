package com.hip.ujr.ujrhip.View.Login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignUpResult
import com.hip.ujr.ujrhip.Etc.StringData.Companion.USER_NAME
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.activity_signup_view.*


class SignUpView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_view)

        initialize()
        setView()
        setMVP()
    }

    private fun initialize() {
    }
    private fun setView() {
        signUpCompleteBtn.setOnClickListener {
            val username = typeNameEtv.text.toString()
            val password = typePassEtv.text.toString()
            val attributes = HashMap<String,String>()
            //속성
            attributes["email"] = typeEmailEtx.text.toString()
            AWSMobileClient.getInstance().signUp(username, password, attributes, null, object : Callback<SignUpResult> {
                override fun onResult(signUpResult: SignUpResult) {
                    runOnUiThread {
                        if (!signUpResult.confirmationState) {
                            val details = signUpResult.userCodeDeliveryDetails
                            Toast.makeText(applicationContext,"Confirm sign-up with: " + details.destination, Toast.LENGTH_SHORT).show()
                            //인증 페이지 넘어가기
                            val intent = Intent(applicationContext, ConfirmView::class.java)
                            intent.putExtra(USER_NAME,username)
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext,"Sign Up Done", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onError(e: Exception) {
                    runOnUiThread {
                        Toast.makeText(applicationContext,"Sign Up Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
    private fun setMVP() {

    }
}
