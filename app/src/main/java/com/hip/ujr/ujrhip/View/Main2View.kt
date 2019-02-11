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
import com.hip.ujr.ujrhip.Etc.AWSCognito
import com.hip.ujr.ujrhip.Etc.AWSDB
import com.hip.ujr.ujrhip.Etc.StringData.Companion.CREATE_ACTIVITY
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.activity_main2_view.*
import kotlinx.android.synthetic.main.fragment_list_fragment_view.*
import kotlinx.android.synthetic.main.fragment_main2_view.view.*


class Main2View : AppCompatActivity() {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var fragment1 = ListFragmentView.newInstance()
    private var fragment2 = HomeFragmentView.newInstance()

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
                    fragment1
                }
                else->{
                    fragment2
                }
            }
        }
        //화면 개수
        override fun getCount(): Int = 2
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        container.currentItem = 0
        fragment1.refreshLayout.isRefreshing = true
        fragment1.refreshData()
        fragment1.refreshLayout.isRefreshing = false
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
//        //네트워크 시간
//        thread{ //truetime 초기화
//            TrueTimeRx.build()
//                .initializeRx("time.google.com")
//                .subscribeOn(Schedulers.io())
//                .subscribe({ date -> Log.v("TAG!", "TrueTime was initialized and we have a time: $date") }) { throwable -> throwable.printStackTrace() }
//        }
        AWSDB.init()
        AWSCognito.init(applicationContext)
    }
    private fun setView(){
        //플로팅 버튼 클릭
        fab.setOnClickListener {
            //게시물 생성 페이지
            fragment1.startActivityForResult(Intent(this,CreateView::class.java), CREATE_ACTIVITY)
        }
        //뷰페이저 어뎁터 연결
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        container.currentItem = 0
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
