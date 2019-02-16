package com.hip.ujr.ujrhip.View.Login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignUpResult
import com.hip.ujr.ujrhip.Etc.StringData.Companion.USER_NAME
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.activity_confirm_view.*


class ConfirmView : AppCompatActivity() {
    var userNmae = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_view)

        initialize()
        setView()
    }

    private fun initialize() {
        userNmae = intent.getStringExtra(USER_NAME)
    }

    private fun setView() {
        ConfirmBtn.setOnClickListener {
            val username = userNmae
            val code = confirmText.text.toString()
            AWSMobileClient.getInstance().confirmSignUp(username, code, object : Callback<SignUpResult> {
                override fun onResult(signUpResult: SignUpResult) {
                    runOnUiThread {
                        Log.d("asdasd", "Sign-up callback state: " + signUpResult.confirmationState)
                        if (!signUpResult.confirmationState) {
                            val details = signUpResult.userCodeDeliveryDetails
                            Toast.makeText(applicationContext,"Confirm sign-up with: " + details.destination, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(applicationContext,"Sign Up Done", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onError(e: Exception) {
                    runOnUiThread {
                        Toast.makeText(applicationContext,"인증번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}
