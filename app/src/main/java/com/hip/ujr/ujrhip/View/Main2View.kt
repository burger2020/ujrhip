package com.hip.ujr.ujrhip.View

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.amazonaws.mobileconnectors.s3.transferutility.TransferService
import com.hip.ujr.ujrhip.Etc.AWSDB
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.activity_main2_view.*
import kotlinx.android.synthetic.main.fragment_main2_view.view.*


class Main2View : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2_view)

        initialize()
        setView()
    }

    //옵션바
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main2_view, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
            if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    //뷰페이저 포지션별 화면 출력 프래그먼트 설정
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when(position){
                0->{
                    PlaceholderFragment.newInstance(position + 1)
                }
                1->{
                    ListFragmentView.newInstance()
                }
                else->{
                    HomeFragmentView.newInstance()
                }
            }
        }
        //화면 개수
        override fun getCount(): Int = 3
    }
    //초기화
    private fun initialize() {
        //액션바 연결
        setSupportActionBar(toolbar_rating)
        //뷰페이저 어뎁터
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        // aws s3 TransferUtility (끊긴 업로드 자동 진행)
        applicationContext.startService(Intent(applicationContext, TransferService::class.java))

        AWSMobileClient.getInstance().initialize(applicationContext, object : Callback<UserStateDetails> {
            override fun onResult(userStateDetails: UserStateDetails) {
                Log.i("MainView", "AWSMobileClient initialized. User State is " + userStateDetails.userState)
//                AWSMobileClient.getInstance().signOut()
            }
            override fun onError(e: Exception) {
                Log.e("MainView", "Initialization error.", e)
            }
        })

        AWSDB.init()
//        AWSS3.uploadWithTransferUtility(applicationContext,"","")
    }
    private fun setView(){
        //플로팅 버튼 클릭
        fab.setOnClickListener { view ->
            //게시물 생성 페이지
            startActivity(Intent(this,CreateView::class.java))
        }
        //뷰페이저 어뎁터 연결
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        container.currentItem = 1
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }
    //
    class PlaceholderFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_main2_view, container, false)
            rootView.section_label.text = getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))
            return rootView
        }
        companion object {
            private val ARG_SECTION_NUMBER = "section_number"

            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
