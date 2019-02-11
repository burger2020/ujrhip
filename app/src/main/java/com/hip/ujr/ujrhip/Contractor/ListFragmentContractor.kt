package com.hip.ujr.ujrhip.Contractor

import com.hip.ujr.ujrhip.Item.postData

interface ListFragmentContractor {
    interface View{
        fun listOptionClick(postData: postData, position: Int)
        fun likeBtnClick(postData: postData, position: Int)
        fun commentBtnClick(postData: postData, position: Int)
    }
    interface Adapter{

    }
}
