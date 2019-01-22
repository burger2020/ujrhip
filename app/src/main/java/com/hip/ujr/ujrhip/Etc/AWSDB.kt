package com.hip.ujr.ujrhip.Etc

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.amazonaws.ClientConfiguration
import com.amazonaws.mobile.auth.core.internal.util.ThreadUtils
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator
import com.amazonaws.services.dynamodbv2.model.Condition
import com.hip.ujr.ujrhip.Item.postData
import android.os.StrictMode
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.lang.Exception


object  AWSDB {
    private var client: AmazonDynamoDBClient? = null

    private lateinit var dynamoDBMapper : DynamoDBMapper

    fun init(){
        client  = Region.getRegion(Regions.AP_NORTHEAST_1) // CRUCIAL
            .createClient(
                AmazonDynamoDBClient::class.java,
                AWSMobileClient.getInstance().credentialsProvider,
                ClientConfiguration()
            )
        dynamoDBMapper = DynamoDBMapper.builder()
            .dynamoDBClient(client)
            .awsConfiguration(AWSMobileClient.getInstance().configuration)
            .build()
    }

    fun <T> createTable(data : T){
        dynamoDBMapper.save(data)
    }

    fun getItem(callBack: AWSDBCallback) {
        val item = postData()

        item.userId = "ㅜ너누ㅜㄴ눈"

        val eav = HashMap<String, AttributeValue>()
        eav[":val1"] = AttributeValue().withS("0")

        val rangeKeyCondition = Condition()
            .withComparisonOperator(ComparisonOperator.BEGINS_WITH)
            .withAttributeValueList(AttributeValue().withS("Trial"))

        val queryExpression = DynamoDBQueryExpression<postData>()
            .withFilterExpression("dateNum > :val1")
            .withExpressionAttributeValues(eav)
            .withLimit(10)
            .withScanIndexForward(false)

        val scanExpression = DynamoDBScanExpression()
            .withLimit(1)
            .withFilterExpression("userId > :val1")
            .withExpressionAttributeValues(eav)

        scanExpression.limit = 1

        //네트워크 사용 쓰레드
        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        ThreadUtils.runOnUiThread {
            val a = dynamoDBMapper.scan(postData::class.java,scanExpression)
//            val a = dynamoDBMapper.query(postData::class.java,queryExpression)
            callBack.loadDataCallback(a)
        }
    }
}