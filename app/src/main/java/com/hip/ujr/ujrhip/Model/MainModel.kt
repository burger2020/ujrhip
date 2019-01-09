package com.hip.ujr.ujrhip.Model

import com.hip.ujr.ujrhip.Contractor.MainContractor
import com.hip.ujr.ujrhip.Etc.AWSS3
import com.hip.ujr.ujrhip.Item.postData

class MainModel : MainContractor.Model{
    val post = postData()
    fun dataSave(photoUrl: String, path: String){
        AWSS3.uploadWithTransferUtility(photoUrl, path)
    }
}