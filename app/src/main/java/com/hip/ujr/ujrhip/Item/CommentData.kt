package com.hip.ujr.ujrhip.Item

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable

@DynamoDBTable(tableName = "commentData")
class CommentData {
    @get:DynamoDBHashKey(attributeName = "Index")
    var index: Long? = null
    @get:DynamoDBAttribute(attributeName = "userName")
    var userName = ""
    @get:DynamoDBAttribute(attributeName = "comment")
    var comment = ""
    @get:DynamoDBAttribute(attributeName = "id")
    var date = 0L
    @get:DynamoDBAttribute(attributeName = "profileUrl")
    var profileUrl = ""
    var test = arrayListOf<String>()

    fun setData(userName: String, comment: String, date: Long, profileUrl: String){
        this.userName = userName
        this.test = arrayListOf("1","2","2,","3","4","55","213131")
        this.comment = comment
        this.date = date
        this.profileUrl = profileUrl
    }
    fun setData(data: CommentData, index: Long){
        this.index = index
        this.userName = data.userName
        this.comment = data.comment
        this.date = data.date
        this.profileUrl = data.profileUrl
        //TODO 처음부터 명시해서 하는게 아니고 그냥 처넣으면 되네,,,
        this.test = arrayListOf("1","2","2,","3","4","55","213131")
    }
}