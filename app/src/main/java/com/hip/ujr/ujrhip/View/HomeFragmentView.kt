package com.hip.ujr.ujrhip.View

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hip.ujr.ujrhip.Adapter.PostListAdapter
import com.hip.ujr.ujrhip.Item.UjrItem
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.fragment_home_fragment_view.view.*
import java.util.*


class HomeFragmentView : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home_fragment_view, container, false)
        val ujrItem = ArrayList<UjrItem>()
        val c = Calendar.getInstance()
        ujrItem.add(UjrItem(
            "",
            "감각의 제국\n위치는 을지로가역 부근\n을지로 힙스터의 시작,.....",
            "을지로",
            c.timeInMillis))
        val postListAdapter = PostListAdapter(context!!,ujrItem)
        view.postListView.adapter = postListAdapter
        view.postListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return view
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragmentView().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
