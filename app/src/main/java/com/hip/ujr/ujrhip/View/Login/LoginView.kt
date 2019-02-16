package com.hip.ujr.ujrhip.View.Login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferService
import com.hip.ujr.ujrhip.Contractor.LoginContractor
import com.hip.ujr.ujrhip.Etc.AWSCognito
import com.hip.ujr.ujrhip.Etc.AWSDB
import com.hip.ujr.ujrhip.R
import com.hip.ujr.ujrhip.View.MainView
import kotlinx.android.synthetic.main.activity_login_view.*
import kotlinx.android.synthetic.main.activity_main2_view.*

class LoginView : AppCompatActivity(), LoginContractor.View {
    private var userId = ""

    private var authenticationHandler = object : AuthenticationHandler {
        override fun onSuccess(cognitoUserSession: CognitoUserSession, device: CognitoDevice?){
            AWSCognito.currSession = cognitoUserSession
//            aws에서 디바이스 저장 안쓰면 null
            if (device != null)
                AWSCognito.newDevice(device)
//            launchUser()
//            유저로그인 정보 저장
            AWSCognito.setUser(userId)
            progressVisible(false)
            signInSuccess()
        }
        override fun getAuthenticationDetails(authenticationContinuation: AuthenticationContinuation, username: String){}
        override fun getMFACode(multiFactorAuthenticationContinuation: MultiFactorAuthenticationContinuation){}
        override fun onFailure(e: Exception){}
        override fun authenticationChallenge(continuation: ChallengeContinuation){}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_view)

        initialize()
        setView()
        setMVP()
    }

    //로그인 완료 후 메인뷰 넘어가
    private fun signInSuccess(){
        val intent = Intent(applicationContext, MainView::class.java)
//        intent.putExtra("",userId)
        startActivity(intent)
    }
    //프로그래스바 보이기
    override fun progressVisible(visible: Boolean) {
        if(visible)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }

    //초기화
    private fun initialize() {
        //액션바 연결
        setSupportActionBar(toolbar_rating)
        // aws s3 TransferUtility (끊긴 업로드 자동 진행)
        applicationContext.startService(Intent(applicationContext, TransferService::class.java))
        //클라이언트 초기화
        AWSMobileClient.getInstance().initialize(applicationContext, object : Callback<UserStateDetails> {
            override fun onResult(userStateDetails: UserStateDetails) {
                Log.i("MainView", "AWSMobileClient initialized. User State is " + userStateDetails.userState)

//                AWSMobileClient.getInstance().signOut()
            }
            override fun onError(e: Exception) { Log.e("MainView", "Initialization error.", e) }
        })
        AWSDB.init()
    }
    private fun setView() {
        //이메일 로그인버튼
        signInBtn.setOnClickListener{
            val intent = Intent(this, SignInView::class.java)
            startActivity(intent)
        }
        //이메일 회원가입 버튼
        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpView::class.java)
            startActivity(intent)
        }
        //페이스북 로그인버튼
//        facebookSignInBtn.setOnClickListener{ facebookSignIn.performClick() }
        facebookSignInBtn.setOnClickListener{ startActivity(Intent(this, MainView::class.java)) }
        //로그인 세션 남아있는지 확인


//        AWSMobileClient.getInstance().si

    }
    private fun setMVP() {

    }
}
