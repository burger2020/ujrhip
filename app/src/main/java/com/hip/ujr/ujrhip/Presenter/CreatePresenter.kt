package com.hip.ujr.ujrhip.Presenter

import com.hip.ujr.ujrhip.Contractor.CreateContractor
import java.lang.ref.WeakReference

class CreatePresenter : CreateContractor.Presenter, CreateContractor.RequirePresenter {
    private var view : WeakReference<CreateContractor.View>? = null  // 뷰
    private var model : CreateContractor.Model? = null // 모델
    //    모델 연결
    fun setModel(model : CreateContractor.Model){
        this.model = model
    }
    // 뷰 연결
    override fun setView(view: CreateContractor.View) {
        this.view = WeakReference(view)
    }
}