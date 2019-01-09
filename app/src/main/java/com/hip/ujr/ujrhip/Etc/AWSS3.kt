@file:Suppress("DEPRECATION")

package com.hip.ujr.ujrhip.Etc

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtilityOptions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.s3.AmazonS3Client
import java.io.File

@SuppressLint("StaticFieldLeak")
object AWSS3 {
    var context: Context? = null

    fun init(context: Context){
        this.context = context
    }
    //업로드
    fun uploadWithTransferUtility(key: String, filePath: String) {
        val options = TransferUtilityOptions()
        //스레드 수
        options.transferThreadPoolSize = 8
        //정지된 전송을 해당 주기로 감시 하여 발견시 실행 시는 시간
        options.transferServiceCheckTimeInterval = 2 * 60 * 1000 // 2-minutes

        val transferUtility = TransferUtility.builder()
            .context(context)
            .awsConfiguration(AWSMobileClient.getInstance().configuration)
            .s3Client(AmazonS3Client(AWSMobileClient.getInstance()))
            .transferUtilityOptions(options)
            .build()

        val uploadObserver = transferUtility.upload(
            key,
            File(filePath)
        )

        // 관찰자에게 수신기를 연결하여 상태 업데이트 및 진행률 알림 가져옴
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (TransferState.COMPLETED === state) {
                    // 업로드 완료

                }
            }
            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                //진행률
                val percentDonef = bytesCurrent.toFloat() / bytesTotal.toFloat() * 100
                val percentDone = percentDonef.toInt()
                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%"
                )
            }
            override fun onError(id: Int, ex: Exception) {
                // 에러
            }
        })

        // 데이터를 폴링하려면 앨리스터를 부착하는 대신 관찰자의 상태 및 진행 상태를 확인하십시오.
        if (TransferState.COMPLETED === uploadObserver.state) {
            // 업로드 완료
        }

        Log.d("YourActivity", "Bytes Transferred: " + uploadObserver.bytesTransferred)
        Log.d("YourActivity", "Bytes Total: " + uploadObserver.bytesTotal)
    }
    fun downloadWithTransferUtility(path: String) {

        val transferUtility = TransferUtility.builder()
            .context(context)
            .awsConfiguration(AWSMobileClient.getInstance().configuration)
            .s3Client(AmazonS3Client(AWSMobileClient.getInstance())).build()

        val downloadObserver =
        transferUtility.download(
            "public/s3Key.txt",
            File(path))


        //TODO DynamoDB 사용법

        // Attach a listener to the observer to get state update and progress notifications
        downloadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (TransferState.COMPLETED === state) {
                    // 다운로드 완료
                }
            }
            override fun onProgressChanged(id: Int,bytesCurrent: Long,bytesTotal: Long) {
                val percentDonef = (bytesCurrent.toFloat()/bytesTotal.toFloat()) * 100
                val percentDone = percentDonef.toInt()

                Log.d("Your Activity",
                    "   ID:$id   bytesCurrent: $bytesCurrent   bytesTotal: $bytesTotal $percentDone%"
                )
            }

            override fun onError(id: Int,ex: java.lang.Exception) {
                // Handle errors
            }
        })

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED === downloadObserver.state) {
            // 다운로드 완료
        }

        Log.d("Your Activity", "Bytes Transferred: " + downloadObserver.bytesTransferred)
        Log.d("Your Activity", "Bytes Total: " + downloadObserver.bytesTotal)
    }
}