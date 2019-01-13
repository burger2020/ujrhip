package com.hip.ujr.ujrhip.View

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList
import com.hip.ujr.ujrhip.Adapter.PostListAdapter
import com.hip.ujr.ujrhip.Etc.AWSDB
import com.hip.ujr.ujrhip.Etc.AWSDBInterface
import com.hip.ujr.ujrhip.Item.postData
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.fragment_list_fragment_view.view.*


class ListFragmentView : Fragment(), AWSDBInterface {
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
    private fun refreshData(){
        AWSDB.getItem(this)
        rootView.refreshLayout.isRefreshing = false
    }
    //인터페이스 콜백
    override fun loadDataCallback(a: PaginatedScanList<postData>) {
        ujrItem.clear()
        ujrItem.addAll(a)
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

    companion object {
        @JvmStatic
        fun newInstance() =
            ListFragmentView().apply {
                arguments = Bundle().apply {
                }
            }
    }
}