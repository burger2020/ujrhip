package com.hip.ujr.ujrhip.Model

import com.hip.ujr.ujrhip.Contractor.CreateContractor
import com.hip.ujr.ujrhip.Etc.AWSS3
import com.hip.ujr.ujrhip.Etc.AWSS3Callback
import com.hip.ujr.ujrhip.Etc.StringData
import com.hip.ujr.ujrhip.Etc.StringData.Companion.EMPTY
import com.hip.ujr.ujrhip.Item.postData

class CreateModel : CreateContractor.Model {
    private var presenter : CreateContractor.Presenter? = null
    var data = postData()
    //프레젠터
    override fun setPresenter(presenter: CreateContractor.Presenter) {
        this.presenter = presenter
    }
    //테이블 데이터 세팅
    override fun setData(userId: String?,date: Long?,password: String?,photoUrl: String?,content: String?) {
        data.setData(userId,date,password,photoUrl,content)
    }
    //사진 저장
    override fun savePhoto(photoUrl: String, path: String, callback: AWSS3Callback) {
        AWSS3.uploadWithTransferUtility(photoUrl, path, callback)
    }
    //사진 등록 없을 시
    override fun emptyPhoto() {
        data.imageUrl = EMPTY
    }
}