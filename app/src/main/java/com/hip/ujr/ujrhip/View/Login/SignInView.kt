package com.hip.ujr.ujrhip.View.Login

import android.R.attr.password
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.results.SignInResult
import com.amazonaws.mobile.client.results.SignInState
import com.hip.ujr.ujrhip.Etc.Util.makeToast
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.activity_sign_in_view.*


class SignInView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_view)

        setView()
    }

    private fun setView() {
        val userName = userIdText.text.toString()
        val password = userPassText.text.toString()
        signInCompleteBtn.setOnClickListener {
            AWSMobileClient.getInstance().signIn(userName, password, null, object : Callback<SignInResult> {
                override fun onResult(signInResult: SignInResult) {
                    runOnUiThread {
                        when (signInResult.signInState) {
                            SignInState.DONE -> makeToast(applicationContext, "Sign-in done.")
                            SignInState.SMS_MFA -> makeToast(applicationContext,"Please confirm sign-in with SMS.")
                            SignInState.NEW_PASSWORD_REQUIRED -> makeToast(applicationContext,"Please confirm sign-in with new password.")
                            else -> makeToast(applicationContext, "Unsupported sign-in confirmation: " + signInResult.signInState)
                        }
                    }
                }
                override fun onError(e: Exception) {
                    Log.e(SignInView::class.java.name, "Sign-in error", e)
                }
            })
        }
    }
}
