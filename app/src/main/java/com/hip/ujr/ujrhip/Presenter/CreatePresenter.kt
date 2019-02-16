package com.hip.ujr.ujrhip.Presenter

import android.content.Context
import android.util.Log
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.hip.ujr.ujrhip.Contractor.CreateContractor
import com.hip.ujr.ujrhip.Item.PostData
import kotlinx.android.synthetic.main.activity_create_view.*
import java.lang.ref.WeakReference

class CreatePresenter : CreateContractor.Presenter, CreateContractor.RequirePresenter {
    private var view : WeakReference<CreateContractor.View>? = null  // 뷰
    private var model : CreateContractor.Model? = null // 모델
    //    모델 연결
    override fun setModel(model : CreateContractor.Model){ this.model = model }
    // 뷰 연결
    override fun setView(view: CreateContractor.View) { this.view = WeakReference(view) }
    //게시물 등록 양식 체크
    override fun checkForm(content: String, userId: String, password: String): Boolean = (userId.length>=2 &&  password.length>=3 && content.length>=3)
    //사진 서버에 저장
    override fun addPhoto(postData: PostData, path: String) {

        model?.setData(postData)
        //경로 지정 되어있으면 사진 저장
        if(path != "")
            model?.savePhoto(postData.imageUrl.toString(), path)
        else
            model?.emptyPhoto()
        runOnUiThread {
            Log.d("post!!!","${model?.createTable()}")
            model?.createTable()
            if(path=="")
                view?.get()?.uploadSuccess()
        }
    }
    //데이터 저장 상태 전달
    override fun tableDataSaveTransfer(state: TransferState) {
        view?.get()?.tableDataSaveTransfer(state)
    }
    //데이터 저장 완료
    override fun successTableDataSave(code: String) {

    }
    override fun getAppContext(): Context = view?.get()?.getAppContext()!!
    override fun getActivityContext(): Context = view?.get()?.getActivityContext()!!
}