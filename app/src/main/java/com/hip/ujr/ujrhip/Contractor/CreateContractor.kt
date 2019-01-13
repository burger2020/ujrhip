package com.hip.ujr.ujrhip.Contractor

import android.content.Context

interface CreateContractor {
    interface View{
        fun getAppContext(): Context
        fun getActivityContext(): Context
    }
    interface Presenter{
        fun setView(view: View)
    }
    interface RequirePresenter{

    }
    interface Model{
        fun setPresenter(presenter: Presenter)
        fun savePhoto(photoUrl: String, path: String)
        fun emptyPhoto()
        fun setData(userId: String?, date: Long?, password: String?, photoUrl: String?, content: String?)
    }
}