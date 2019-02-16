@file:Suppress("DEPRECATION")

package com.hip.ujr.ujrhip.Etc

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import java.io.File

@SuppressLint("StaticFieldLeak")
object AWSS3 {
    var context: Context? = null

    fun init(context: Context){
        this.context = context
    }
    fun removeWithTransferUtility(path: String){
        val transferUtility = TransferUtility.builder()
            .context(context)
            .awsConfiguration(AWSMobileClient.getInstance().configuration)
            .s3Client(AmazonS3Client(AWSMobileClient.getInstance())).build()

        transferUtility
    }
    //다운
    fun downloadWithTransferUtility(path: String) {
        val transferUtility = TransferUtility.builder()
            .context(context)
            .awsConfiguration(AWSMobileClient.getInstance().configuration)
            .s3Client(AmazonS3Client(AWSMobileClient.getInstance())).build()

        val downloadObserver =
        transferUtility.download(
            "public/s3Key.txt",
            File(path))

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