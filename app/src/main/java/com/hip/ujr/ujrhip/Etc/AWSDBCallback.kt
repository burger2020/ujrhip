package com.hip.ujr.ujrhip.Etc

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList
import com.hip.ujr.ujrhip.Item.postData

interface AWSDBCallback {
    fun loadDataCallback(data: PaginatedScanList<postData>)
}