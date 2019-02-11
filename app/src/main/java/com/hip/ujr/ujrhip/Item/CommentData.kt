package com.hip.ujr.ujrhip.Item

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable

@DynamoDBTable(tableName = "commentTable")
class CommentData {
    @get:DynamoDBHashKey(attributeName = "partition")
    var partition: String? = null
    @get:DynamoDBAttribute(attributeName = "index")
    var index: Long? = null
}