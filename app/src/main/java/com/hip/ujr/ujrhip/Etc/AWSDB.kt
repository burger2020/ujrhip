package com.hip.ujr.ujrhip.Etc

import com.amazonaws.ClientConfiguration
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient

object AWSDB {
    private var client: AmazonDynamoDBClient? = null

    private var dynamoDBMapper : DynamoDBMapper? = null

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
        dynamoDBMapper?.save(data)
    }

    //얘가 있어야함
//       if (android.os.Build.VERSION.SDK_INT > 9) {
//            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//            StrictMode.setThreadPolicy(policy)
//        }
//        Table.loadTable(client, "postTable")

//        val client = AmazonDynamoDBClient(AWSMobileClient.getInstance().credentialsProvider)
}