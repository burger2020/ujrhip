package com.hip.ujr.ujrhip.Etc

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList
import com.hip.ujr.ujrhip.Item.postData

interface AWSDBInterface {
    fun loadDataCallback(a: PaginatedScanList<postData>)
}