package com.hip.ujr.ujrhip.Item

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable
import java.io.Serializable

@DynamoDBTable(tableName = "postData")
class postData: Serializable {
    @get:DynamoDBHashKey(attributeName = "postIndex")
    var index: Long? = null
    @get:DynamoDBAttribute(attributeName = "postDate")
    var date: Long? = null
    @get:DynamoDBAttribute(attributeName = "postUserId")
    var userId: String? = null
    @get:DynamoDBAttribute(attributeName = "postPassword")
    var password: String? = null
    @get:DynamoDBAttribute(attributeName = "postImageUrl")
    var imageUrl: String? = null
    @get:DynamoDBAttribute(attributeName = "postContent")
    var content: String? = null

    fun setData(userId: String?,date: Long?,password: String?,imageUrl: String?,content: String?){
        this.userId = userId
        this.date = date
        this.password = password
        this.imageUrl = imageUrl
        this.content = content
    }
    fun saveData(data: postData,idx: Long){
        this.userId = data.userId
        this.date = data.date
        this.password = data.password
        this.imageUrl = data.imageUrl
        this.content = data.content
        this.index = idx
    }
    object DateComparator : Comparator<postData> {
        override fun compare(p1: postData, p2: postData): Int = p2.date!!.compareTo(p1.date!!)
    }
}