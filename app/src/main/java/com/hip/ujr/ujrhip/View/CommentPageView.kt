package com.hip.ujr.ujrhip.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hip.ujr.ujrhip.Etc.StringData.Companion.POSITION
import com.hip.ujr.ujrhip.Etc.StringData.Companion.POST_DATA
import com.hip.ujr.ujrhip.Item.postData
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.activity_comment_page_view.*

class CommentPageView : AppCompatActivity() {
    lateinit var postData: postData
    var position: Int = 0
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
    }
    private fun setView() {
        backBtn.setOnClickListener { finish() }
    }
    private fun setMVP() {
    }
}
