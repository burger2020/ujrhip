package com.hip.ujr.ujrhip.View

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.hip.ujr.ujrhip.Adapter.PostListAdapter
import com.hip.ujr.ujrhip.Etc.AWSDB
import com.hip.ujr.ujrhip.Etc.AWSDBCallback
import com.hip.ujr.ujrhip.Etc.StringData.Companion.CREATE_ACTIVITY
import com.hip.ujr.ujrhip.Etc.StringData.Companion.UPLOAD_COMPLETED
import com.hip.ujr.ujrhip.Item.postData
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.fragment_list_fragment_view.view.*


class ListFragmentView : Fragment(), AWSDBCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private lateinit var rootView : View

    private var ujrItem : ArrayList<postData> = arrayListOf()
    private lateinit var postListAdapter : PostListAdapter

    private val handlerThread = Handler()

    private var addListFlag = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_list_fragment_view, container, false)

        initialize()
        setView()
        setMVP()

        return rootView
    }
    //리사이클러뷰 스크롤 막기
    private fun listScroll(flag: Boolean){
        if(!flag)
            rootView.postListView.setOnTouchListener(object : View.OnTouchListener{
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    return true
                }
            })
        else
            rootView.postListView.setOnTouchListener(null)

    }
    //인터페이스 콜백  리스트 새로고침/초기화
    override fun loadDataCallback(data: ArrayList<postData>) {
        ujrItem.clear()
        ujrItem.addAll(data)
        handlerThread.post {
            postListAdapter.notifyDataSetChanged()
            rootView.refreshLayout.isRefreshing = false

        }
        listScroll(true)
    }
    //인터페이스 콜백 리스트 추가
    override fun addDataCallback(data: ArrayList<postData>) {
        ujrItem.addAll(data)
        handlerThread.post {
            postListAdapter.notifyDataSetChanged()
            rootView.refreshLayout.isRefreshing = false
            addListFlag = true
        }
        listScroll(true)
    }
    //리스트 새로고침
    fun refreshData(){
        AWSDB.getList(this)
        listScroll(false)
    }
    //    기본 세팅 초기화
    private fun initialize() {
        postListAdapter = PostListAdapter(rootView.context,ujrItem)
        rootView.postListView.adapter = postListAdapter
        rootView.postListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        AWSDB.getList(this)
    }
    //하단 내릴시 리스트 추가
    private fun addList(){
        if(addListFlag && ujrItem[ujrItem.size - 1].index!! > 1) {
            addListFlag = false
            listScroll(false)
            AWSDB.addList(this, ujrItem[ujrItem.size - 1].index!!)
        }
    }
    //뷰 초기화
    private fun setView() {
        rootView.refreshLayout.setOnRefreshListener { refreshData() }

        rootView.postListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = rootView.postListView.adapter?.itemCount?.minus(1)
                if (lastVisibleItemPosition == itemTotalCount) {
                    addList()
                }
            }
        })
    }
    //mvp 초기화
    private fun setMVP() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CREATE_ACTIVITY->{
                when(resultCode){
                    //업로드 성공 후 돌아오면 게시물 초기화
                    UPLOAD_COMPLETED->{
                        refreshData()
                        Snackbar.make(rootView,"게시물이 등록되었습니다.", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ListFragmentView().apply {
                arguments = Bundle().apply {
                }
            }
    }
}