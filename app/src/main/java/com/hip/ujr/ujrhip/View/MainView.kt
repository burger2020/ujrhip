package com.hip.ujr.ujrhip.View

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hip.ujr.ujrhip.Contractor.MainContractor
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.activity_main.*

class MainView : AppCompatActivity(), MainContractor.View {
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        //바탐네비 아이템 선택 리스너
        when (item.itemId) {
            R.id.navigation_home -> selectedFragment = HomeFragmentView.newInstance("","")
            R.id.navigation_dashboard -> selectedFragment = CreateFragmentView.newInstance("","")
            R.id.navigation_notifications -> selectedFragment = ListFragmentView.newInstance("","")
        }
        //프래그먼트 전환
        val transaction = supportFragmentManager.beginTransaction()
        if (selectedFragment != null) {
            transaction.replace(R.id.fragmentLayer, selectedFragment)
        }
        transaction.commit()
        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setView()
    }

    private fun setView() {
        Glide.with(this)
            .load(resources.getDrawable(R.drawable.ujrhpi_img))
            .apply(RequestOptions().centerCrop())
            .into(mainImageView)
        //바탐네비 아이템 선텍 리스너
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
