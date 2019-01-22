package com.hip.ujr.ujrhip.Contractor

import android.content.Context
import com.hip.ujr.ujrhip.Etc.AWSS3Callback

interface CreateContractor {
    interface View{
        fun getAppContext(): Context
        fun getActivityContext(): Context
        fun progressbarVisible(visible: Int)
    }
    interface Presenter{
        fun setView(view: View)
    }
    interface RequirePresenter{

    }
    interface Model{
        fun setPresenter(presenter: Presenter)
        fun emptyPhoto()
        fun setData(userId: String?, date: Long?, password: String?, photoUrl: String?, content: String?)
        fun savePhoto(photoUrl: String, path: String, callback: AWSS3Callback)
    }
}