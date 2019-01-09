package com.hip.ujr.ujrhip.View

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amazonaws.ClientConfiguration
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.hip.ujr.ujrhip.Adapter.PostListAdapter
import com.hip.ujr.ujrhip.Item.postData
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.fragment_list_fragment_view.view.*
import java.util.*


class ListFragmentView : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }


    private lateinit var rootView : View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_list_fragment_view, container, false)
        val ujrItem = ArrayList<postData>()
        val c = Calendar.getInstance()

        //데이터 가져오기

        for(i in 0 until 5)
            ujrItem.add(postData(
                "Id",
                c.timeInMillis,
                "123123",
                "을지로",
                "위치는 을지로가역 부근\n을지로"))

        val postListAdapter = PostListAdapter(context!!,ujrItem)
        rootView.postListView.adapter = postListAdapter
        rootView.postListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        initialize()
        setView()
        setMVP()


        return rootView
    }
    //    기본 세팅 초기화
    private fun initialize() {
    }
    //    뷰 초기화
    private fun setView() {
        //사진 선택
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