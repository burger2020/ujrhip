package com.hip.ujr.ujrhip.Item

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable


@DynamoDBTable(tableName = "postTable")
data class postData(@get:DynamoDBHashKey var userId: String? = null,
              @get:DynamoDBAttribute var date: Long? = null,
              @get:DynamoDBAttribute var password: String? = null,
              @get:DynamoDBAttribute var photoUrl: String? = null,
              @get:DynamoDBAttribute var content: String? = null){
    fun a(){

    }
}