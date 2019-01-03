package com.hip.ujr.ujrhip.Item

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable


@DynamoDBTable(tableName = "postTable")

class TestClass {
    @get:DynamoDBHashKey
    var userId: String? = null
    @get:DynamoDBAttribute
    var date: Long? = null
    @get:DynamoDBAttribute
    var content: String? = null
}