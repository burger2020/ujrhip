package com.hip.ujr.ujrhip.View

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.hip.ujr.ujrhip.Adapter.CommentListAdapter
import com.hip.ujr.ujrhip.Etc.StringData.Companion.EMPTY
import com.hip.ujr.ujrhip.Etc.StringData.Companion.POSITION
import com.hip.ujr.ujrhip.Etc.StringData.Companion.POST_DATA
import com.hip.ujr.ujrhip.Item.CommentData
import com.hip.ujr.ujrhip.Item.postData
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.activity_comment_page_view.*

class CommentPageView : AppCompatActivity() {
    lateinit var postData: postData
    var position: Int = 0
    private lateinit var commentAdapter: CommentListAdapter
    private val commentDataList = arrayListOf<CommentData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_page_view)

        initialize()
        setView()
        setMVP()
    }

    private fun initialize() {
        postData = intent.getSerializableExtra(POST_DATA) as postData
        position = intent.getIntExtra(POSITION,0)

        commentAdapter = CommentListAdapter(applicationContext, commentDataList)

        for(i in 0 .. 9){
            val text = CommentData()
            text.index = 1
            text.comment = "테스트입니다!@#!@#!@#$i"
            text.date = System.currentTimeMillis()
            text.userName = "테스트$i"
            text.profileUrl = EMPTY
            commentDataList.add(text)
        }
    }
    private fun setView() {
        backBtn.setOnClickListener { finish() }
        commentList.adapter = commentAdapter
        commentList.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
    }
    private fun setMVP() {
    }
}
