package com.hip.ujr.ujrhip.View

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferService
import com.hip.ujr.ujrhip.Contractor.MainContractor
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.activity_main.*


@Suppress("DEPRECATION")
class MainView : AppCompatActivity(), MainContractor.View {

    lateinit var mAWSAppSyncClient : AWSAppSyncClient
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        //바탐네비 아이템 선택 리스너
        when (item.itemId) {
            R.id.navigationPost -> selectedFragment = ListFragmentView.newInstance()
            R.id.navigationMember -> selectedFragment = HomeFragmentView.newInstance()
            R.id.navigationAttend -> selectedFragment = CreateFragmentView.newInstance()
            R.id.navigationCreate -> selectedFragment = CreateFragmentView.newInstance()
        }
        //프래그먼트 전환
        if (selectedFragment != null) {
            setFragment(selectedFragment)
        }
        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()
        setView()
        setFragment(ListFragmentView.newInstance())
    }

    private fun initialize() {
        // aws s3 TransferUtility (끊긴 업로드 자동 진행)
        applicationContext.startService(Intent(applicationContext, TransferService::class.java))

        AWSMobileClient.getInstance().initialize(applicationContext, object : Callback<UserStateDetails> {
            override fun onResult(userStateDetails: UserStateDetails) {
                Log.i("MainView", "AWSMobileClient initialized. User State is " + userStateDetails.userState)
            }
            override fun onError(e: Exception) {
                Log.e("MainView", "Initialization error.", e)
            }
        })
//        AWSS3.uploadWithTransferUtility(applicationContext,"","")
    }

    private fun setFragment(selectedFragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentLayer, selectedFragment)
        transaction.commit()
        //바탐네비게이션 옵션
        navigation.enableShiftingMode(false)
        navigation.enableAnimation(false)
        navigation.setTextVisibility(false)

    }
    private fun setView() {
        //바탐네비 아이템 선텍 리스너
        navigation.onNavigationItemSelectedListener = mOnNavigationItemSelectedListener
    }
}
