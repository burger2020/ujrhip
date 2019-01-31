package com.hip.ujr.ujrhip.Etc

import com.hip.ujr.ujrhip.Item.postData

interface AWSDBCallback {
    fun loadDataCallback(data: ArrayList<postData>)
    fun addDataCallback(data: ArrayList<postData>)
}