package com.hip.ujr.ujrhip.Contractor

import android.content.Context
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.hip.ujr.ujrhip.Item.PostData

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
        fun setModel(model: Model)
        fun checkForm(content: String, userId: String, password: String): Boolean
        fun addPhoto(postData: PostData, path: String)
    }
    interface RequirePresenter{
        fun getAppContext(): Context
        fun getActivityContext(): Context
        fun successTableDataSave(code: String)
        fun tableDataSaveTransfer(state: TransferState)
    }
    interface Model{
        fun setPresenter(presenter: RequirePresenter)
        fun emptyPhoto()
        fun setData(postData: PostData)
        fun savePhoto(photoUrl: String, path: String)
        fun createTable()
        var postData: PostData
    }
}