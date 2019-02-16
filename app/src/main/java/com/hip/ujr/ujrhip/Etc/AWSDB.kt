package com.hip.ujr.ujrhip.Etc

import android.annotation.SuppressLint
import android.os.StrictMode
import android.util.Log
import com.amazonaws.ClientConfiguration
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
import com.hip.ujr.ujrhip.Etc.StringData.Companion.COMMENT
import com.hip.ujr.ujrhip.Etc.StringData.Companion.POST
import com.hip.ujr.ujrhip.Etc.StringData.Companion.POST_TYPE
import com.hip.ujr.ujrhip.Etc.StringData.Companion.addListSize
import com.hip.ujr.ujrhip.Etc.StringData.Companion.initialListSize
import com.hip.ujr.ujrhip.Item.CommentData
import com.hip.ujr.ujrhip.Item.PostIndex
import com.hip.ujr.ujrhip.Item.PostData
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
    //테이블에 데이터 넣기
    fun <T> createPostTable(data : T, partition: String){
        val postIndex = PostIndex()
        postIndex.partition = partition

        val queryExpression = DynamoDBQueryExpression<PostIndex>()
            .withHashKeyValues(postIndex)
//                .withExpressionAttributeValues(eav)
            .withScanIndexForward(false)

        thread {
            dynamoDBMapper.query(PostIndex::class.java,queryExpression).forEach {
                val index = it.index!!
                postIndex.index = index+1
                dynamoDBMapper.save(postIndex)
                when(partition){
                    POST->{
                        val postData = data as PostData
                        Log.d("postData!!!!", "$postData")
                        dynamoDBMapper.save(postData)
                    }
                    COMMENT->{
                        val commentData = CommentData()
                        commentData.setData(data as CommentData, index)
                        dynamoDBMapper.save(commentData)
                    }
                }
            }
        }
    }
    fun addData(){
        dynamoDBMapper.batchSave()
    }
    //테이블에서 데이터 가져오기
    @SuppressLint("ObsoleteSdkInt")
    fun getList(callBack: AWSDBCallback, partition: String) {
        thread {
            val postIndex = PostIndex()
            postIndex.partition = partition
            val queryExpression = DynamoDBQueryExpression<PostIndex>()
                .withHashKeyValues(postIndex)
                .withScanIndexForward(false)

//            var index: Long
//            dynamoDBMapper.query(PostIndex::class.java,queryExpression).forEach {
//                index = it.index!!
//
//                index -= initialListSize
//                if(index<1)
//                    index = 1
//                val eav = HashMap<String, AttributeValue>()
//                eav[":val1"] = AttributeValue().withN("$index")

                //네트워크 사용 쓰레드
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                }

                val rangeKeyCondition = Condition()
                    .withComparisonOperator(ComparisonOperator.BETWEEN.toString())
                    .withAttributeValueList(
//                        AttributeValue().withN("${index+initialListSize}"),
//                        AttributeValue().withN("$index")
                    )
                if(partition == POST) {
                    Log.d("type!!@#","post")
                    val postData = PostData()
                    postData.type = POST_TYPE
                    val queryExpression = DynamoDBQueryExpression<PostData>()
                        .withHashKeyValues(postData)
                        .withScanIndexForward(false)
                    val scanExpression = DynamoDBScanExpression()
//                        .with
//                        .withFilterExpression("postIndex >= :val1")
//                        .withExpressionAttributeValues(eav)
                    val a = arrayListOf<PostData>()
                    dynamoDBMapper.query(PostData::class.java, queryExpression).forEach { it1 ->
                        a.add(it1)
                        //정렬
                        a.sortWith(PostData.DateComparator)
                        Log.d("a_date size: ", "${a.size}")
                        callBack.loadDataCallback(a)
                    }
                }else if(partition == COMMENT){
//                    eav[":val2"] = AttributeValue().withS("ㅓ너ㅓ아나")
//                    val scanExpression = DynamoDBScanExpression()
//                        .withFilterExpression("comment = :val2")
//                        .withExpressionAttributeValues(eav)
//                    val a = arrayListOf<CommentData>()
//                    dynamoDBMapper.scan(CommentData::class.java, scanExpression).forEach { it1 ->
//                        a.add(it1)
//                        //정렬
////                        a.sortWith(postData.DateComparator)
//                        Log.d("a_date size: ", "${a.size}")
//                        callBack.loadDataCallback(a)
//                    }
                }
//            }
        }
    }
    //TODO 데이터 업데이트 (좋아요, 댓글 개수)
    fun updateList(){

    }
    //삭제
    fun deleteList(postData: PostData){
        thread {
            dynamoDBMapper.delete(postData)
        }
    }
    //스크롤 하단 내릴시 리스트 추가
    @SuppressLint("ObsoleteSdkInt")
    fun addList(callBack: AWSDBCallback){
        thread {
//            var endIndex = lastIndex-addListSize
//            if(endIndex<1)
//                endIndex = 1

            val eav = HashMap<String, AttributeValue>()
//            eav[":val1"] = AttributeValue().withN("$lastIndex")
//            eav[":val2"] = AttributeValue().withN("$endIndex")

            //네트워크 사용 쓰레드
            if (android.os.Build.VERSION.SDK_INT > 9) {
                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
            }

            val scanExpression = DynamoDBScanExpression()
                .withFilterExpression("postIndex <= :val1 and postIndex >= :val2")
                .withExpressionAttributeValues(eav)
            val a = arrayListOf<PostData>()
            dynamoDBMapper.scan(PostData::class.java,scanExpression).forEach {
                a.add(it)
                //정렬
                a.sortWith(PostData.DateComparator)
                Log.d("a_date_add size: ","${a.size}")
                callBack.addDataCallback(a)
            }
        }
    }
}