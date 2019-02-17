package com.hip.ujr.ujrhip.Contractor

import com.hip.ujr.ujrhip.Item.PostData

interface ListFragmentContractor {
    interface View{
        fun listOptionClick(postData: PostData, position: Int)
        fun likeBtnClick(PostData: PostData, position: Int)
        fun commentBtnClick(PostData: PostData, position: Int)
    }
    interface Adapter{

    }
}
