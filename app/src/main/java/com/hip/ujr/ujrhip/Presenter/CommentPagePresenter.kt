package com.hip.ujr.ujrhip.Presenter

import android.content.Context
import android.util.Log
import com.hip.ujr.ujrhip.Contractor.CommentPageContractor
import com.hip.ujr.ujrhip.Item.CommentData
import java.lang.ref.WeakReference

class CommentPagePresenter: CommentPageContractor.Presenter, CommentPageContractor.PresenterRequire {
    private var view: WeakReference<CommentPageContractor.View>? = null
    private var model: CommentPageContractor.Model? = null

    override fun setView(view: CommentPageContractor.View){ this.view = WeakReference(view) }
    override fun setModel(model: CommentPageContractor.Model){ this.model = model }
    //댓글 입력 체크
    override fun checkComment(comment: String) {
        //공백만 있음
        view?.get()?.commentIsEmpty(comment.isEmpty() || comment.replace(" ","").isEmpty())
    }
    //코멘트 가져오기
    override fun getCommentList() {
        Log.d("data_size","11111")
        model?.getCommentList()
    }
    //모델에서 가져온 데이터 뷰로 전달
    override fun dataLoadCallBack(commentDataList: ArrayList<CommentData>) {
        Log.d("data_size","${commentDataList.size}")
        view?.get()?.dataLoad(commentDataList)
    }

    //댓글 등록
    override fun sendComment(comment: String) { model?.sendComment(comment) }
    override fun getActivityContext(): Context = view?.get()?.getActivityContext()!!
    override fun getAppContext(): Context = view?.get()?.getAppContext()!!
}