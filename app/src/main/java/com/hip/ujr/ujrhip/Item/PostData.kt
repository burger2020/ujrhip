package com.hip.ujr.ujrhip.Item

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable
import java.io.Serializable

@DynamoDBTable(tableName = "Post")
data class PostData(@get:DynamoDBHashKey(attributeName = "Type")
                    var type: String? = null,
                    @get:DynamoDBRangeKey(attributeName = "Date")
                    var date: Long? = null,
                    @get:DynamoDBAttribute(attributeName = "Password")
                    var password: String? = null,
                    @get:DynamoDBAttribute(attributeName = "UserId")
                    var userId: String? = null,
                    @get:DynamoDBAttribute(attributeName = "Content")
                    var content: String? = null,
                    @get:DynamoDBAttribute(attributeName = "ImageUrl")
                    var imageUrl: String? = null): Serializable {
    constructor():this("",0L,"","","","")
    fun setData(type: String?, date: Long?, password: String?, userId: String?, content: String?, imageUrl: String?){
        this.type = type
        this.date = date
        this.password = password
        this.userId = userId
        this.content = content
        this.imageUrl = imageUrl
    }
    object DateComparator : Comparator<PostData> {
        override fun compare(p1: PostData, p2: PostData): Int = p2.date!!.compareTo(p1.date!!)
    }
}