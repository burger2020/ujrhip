package com.hip.ujr.ujrhip.Item

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable


@DynamoDBTable(tableName = "postData")
class postData {
    @get:DynamoDBHashKey(attributeName = "date")
    var date: Long? = null
    @get:DynamoDBAttribute(attributeName = "userId")
    var userId: String? = null
    @get:DynamoDBAttribute(attributeName = "password")
    var password: String? = null
    @get:DynamoDBAttribute(attributeName = "imageUrl")
    var imageUrl: String? = null
    @get:DynamoDBAttribute(attributeName = "content")
    var content: String? = null

    fun setData(userId: String?,date: Long?,password: String?,photoUrl: String?,content: String?){
        this.userId = userId
        this.date = date
        this.password = password
        this.imageUrl = photoUrl
        this.content = content
    }
}