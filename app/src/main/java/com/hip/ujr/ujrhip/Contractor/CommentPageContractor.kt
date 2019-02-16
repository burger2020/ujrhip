package com.hip.ujr.ujrhip.Contractor

import android.content.Context
import com.hip.ujr.ujrhip.Item.CommentData

interface CommentPageContractor {
    interface View{
        fun getAppContext(): Context
        fun getActivityContext(): Context
        fun commentIsEmpty(flag: Boolean)
        fun dataLoad(commentDataList: ArrayList<CommentData>)
    }
    interface Presenter{
        fun setView(view: CommentPageContractor.View)
        fun setModel(model: Model)
        fun sendComment(comment: String)
        fun checkComment(comment: String)
        fun getCommentList()
    }
    interface PresenterRequire{
        fun getAppContext(): Context
        fun getActivityContext(): Context
        fun dataLoadCallBack(commentDataList: ArrayList<CommentData>)
    }
    interface Model{
        fun setPresenter(presenter: CommentPageContractor.PresenterRequire)
        fun sendComment(comment: String)
        fun getCommentList()
    }
}