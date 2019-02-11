package com.hip.ujr.ujrhip.Item

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable

@DynamoDBTable(tableName = "commentTable")
class CommentData {
    @get:DynamoDBHashKey(attributeName = "index")
    var index: Long? = null
    @get:DynamoDBAttribute(attributeName = "userName")
    var userName = ""
    @get:DynamoDBAttribute(attributeName = "comment")
    var comment = ""
    @get:DynamoDBAttribute(attributeName = "date")
    var date = 0L
    @get:DynamoDBAttribute(attributeName = "profileUrl")
    var profileUrl = ""

}