package com.hip.ujr.ujrhip.Etc

import android.os.StrictMode
import android.util.Log
import com.amazonaws.ClientConfiguration
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator
import com.amazonaws.services.dynamodbv2.model.Condition
import com.hip.ujr.ujrhip.Etc.StringData.Companion.addListSize
import com.hip.ujr.ujrhip.Etc.StringData.Companion.initialListSize
import com.hip.ujr.ujrhip.Item.PostIndex
import com.hip.ujr.ujrhip.Item.postData
import kotlin.concurrent.thread


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
        val postIndex = PostIndex()
        postIndex.partition = "partition"

        val queryExpression = DynamoDBQueryExpression<PostIndex>()
            .withHashKeyValues(postIndex)
//                .withExpressionAttributeValues(eav)
            .withScanIndexForward(false)

        thread {
            dynamoDBMapper.query(PostIndex::class.java,queryExpression).forEach {
                val postData = postData()
                val index = it.index!!
                postIndex.index = index+1
                dynamoDBMapper.save(postIndex)
                postData.saveData(data as postData, index)
                dynamoDBMapper.save(postData)
            }
        }
    }

    fun getList(callBack: AWSDBCallback) {
        thread {

            val postIndex = PostIndex()
            postIndex.partition = "partition"
            val queryExpression = DynamoDBQueryExpression<PostIndex>()
                .withHashKeyValues(postIndex)
                .withScanIndexForward(false)

            var index: Long
            dynamoDBMapper.query(PostIndex::class.java,queryExpression).forEach {
                index = it.index!!

                Log.d("a_date: ","$index")

                index -= initialListSize
                if(index<1)
                    index = 1
                val eav = HashMap<String, AttributeValue>()
                eav[":val1"] = AttributeValue().withN("$index")

                Log.d("a_date: ","$index")
                //네트워크 사용 쓰레드
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                }

                val rangeKeyCondition = Condition()
                    .withComparisonOperator(ComparisonOperator.BETWEEN.toString())
                    .withAttributeValueList(
                        AttributeValue().withN("${index+initialListSize}"),
                        AttributeValue().withN("$index")
                    )

                val scanExpression = DynamoDBScanExpression()
                    .withFilterExpression("postIndex >= :val1")
                    .withExpressionAttributeValues(eav)
                val a = arrayListOf<postData>()
                dynamoDBMapper.scan(postData::class.java,scanExpression).forEach {
                    a.add(it)
                    //정렬
                    a.sortWith(postData.DateComparator)
                    Log.d("a_date size: ","${a.size}")
                    callBack.loadDataCallback(a)
                }
            }
        }
    }
    //스크롤 하단 내릴시 리스트 추가
    fun addList(callBack: AWSDBCallback, lastIndex: Long){
        thread {
            var endIndex = lastIndex-addListSize
            if(endIndex<1)
                endIndex = 1

            val eav = HashMap<String, AttributeValue>()
            eav[":val1"] = AttributeValue().withN("$lastIndex")
            eav[":val2"] = AttributeValue().withN("$endIndex")

            //네트워크 사용 쓰레드
            if (android.os.Build.VERSION.SDK_INT > 9) {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
            }

            val scanExpression = DynamoDBScanExpression()
                .withFilterExpression("postIndex <= :val1 and postIndex >= :val2")
                .withExpressionAttributeValues(eav)
            val a = arrayListOf<postData>()
            dynamoDBMapper.scan(postData::class.java,scanExpression).forEach {
                a.add(it)
                //정렬
                a.sortWith(postData.DateComparator)
                Log.d("a_date_add size: ","${a.size}")
                callBack.addDataCallback(a)
            }
        }
    }
}