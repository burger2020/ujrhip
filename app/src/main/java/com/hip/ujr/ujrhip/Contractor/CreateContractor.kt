package com.hip.ujr.ujrhip.Contractor

import android.content.Context
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.hip.ujr.ujrhip.Item.postData

interface CreateContractor {
    interface View{
        fun getAppContext(): Context
        fun getActivityContext(): Context
        fun progressbarVisible(visible: Int)
        fun uploadSuccess()
        fun tableDataSaveTransfer(state: TransferState)
    }
    interface Presenter{
        fun setView(view: View)
        fun checkForm(content: String, userId: String, password: String): Boolean
        fun addPhoto(userId: String, password: String, content: String, path: String)
    }
    interface RequirePresenter{
        fun getAppContext(): Context
        fun getActivityContext(): Context
        fun successTableDataSave(code: String)
        fun tableDataSaveTransfer(state: TransferState)
    }
    interface Model{
        var postData: postData
        fun setPresenter(presenter: RequirePresenter)
        fun emptyPhoto()
        fun setData(userId: String?, date: Long?, password: String?, photoUrl: String?, content: String?)
        fun savePhoto(photoUrl: String, path: String)
        fun createTable(postData: postData)
    }
}