package com.hip.ujr.ujrhip.Model

import android.util.Log
import com.hip.ujr.ujrhip.Contractor.CommentPageContractor
import com.hip.ujr.ujrhip.Etc.AWSDB
import com.hip.ujr.ujrhip.Etc.AWSDBCallback
import com.hip.ujr.ujrhip.Etc.StringData
import com.hip.ujr.ujrhip.Etc.StringData.Companion.COMMENT
import com.hip.ujr.ujrhip.Etc.StringData.Companion.EMPTY
import com.hip.ujr.ujrhip.Item.CommentData

@Suppress("UNCHECKED_CAST")
class CommentPageModel: CommentPageContractor.Model, AWSDBCallback{
    private lateinit var presenter: CommentPageContractor.PresenterRequire

    override fun setPresenter(presenter: CommentPageContractor.PresenterRequire) { this.presenter = presenter }
    //댓글 리스트 가져오기
    override fun getCommentList() {
        AWSDB.getList(this, COMMENT)
    }
    //TODO 회원가입 절차넣고 회원 이름 들어가도록 변경
    override fun sendComment(comment: String) {
        val test = CommentData()
        test.setData("tester",comment, System.currentTimeMillis(), EMPTY)
        AWSDB.createPostTable(test, StringData.COMMENT)
    }
    override fun <T> loadDataCallback(data: ArrayList<T>) {
        Log.d("data_size","11111")
        Log.d("data_size","${(data as ArrayList<CommentData>).size}")
        presenter.dataLoadCallBack(data as ArrayList<CommentData>)
    }
    override fun <T> addDataCallback(data: ArrayList<T>) {

    }
}