package com.hip.ujr.ujrhip.View

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_list_fragment_view, container, false)

        initialize()
        setView()
        setMVP()

        return rootView
    }
    fun refreshData(){
        AWSDB.getItem(this)
        rootView.refreshLayout.isRefreshing = false
    }
    //인터페이스 콜백
    override fun loadDataCallback(data: ArrayList<postData>) {
        ujrItem.clear()
        ujrItem.addAll(data)
        postListAdapter.notifyDataSetChanged()
    }
    //    기본 세팅 초기화
    private fun initialize() {
        postListAdapter = PostListAdapter(rootView.context,ujrItem)
        rootView.postListView.adapter = postListAdapter
        rootView.postListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        AWSDB.getItem(this)
    }
    //뷰 초기화
    private fun setView() {
        rootView.refreshLayout.setOnRefreshListener { refreshData() }
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