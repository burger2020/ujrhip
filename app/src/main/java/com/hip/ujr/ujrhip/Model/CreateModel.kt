package com.hip.ujr.ujrhip.Model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtilityOptions
import com.amazonaws.services.s3.AmazonS3Client
import com.hip.ujr.ujrhip.Contractor.CreateContractor
import com.hip.ujr.ujrhip.Etc.AWSDB
import com.hip.ujr.ujrhip.Etc.AWSS3
import com.hip.ujr.ujrhip.Etc.StringData.Companion.EMPTY
import com.hip.ujr.ujrhip.Item.postData
import java.io.*

class CreateModel : CreateContractor.Model {
    override var postData = postData()
    private var presenter : CreateContractor.RequirePresenter? = null
    //프레젠터
    override fun setPresenter(presenter: CreateContractor.RequirePresenter) {
        this.presenter = presenter
    }
    //테이블 데이터 세팅
    override fun setData(userId: String?,date: Long?,password: String?,photoUrl: String?,content: String?) {
        postData.setData(userId,date,password,photoUrl,content)
    }
    //사진 저장
    override fun savePhoto(photoUrl: String, path: String) {
        uploadWithTransferUtility(photoUrl, path)
    }
    //사진 등록 없을 시
    override fun emptyPhoto() {
        postData.imageUrl = EMPTY
    }
    override fun createTable(postData: postData) {
        AWSDB.createTable(postData)
    }
    //업로드
    private fun uploadWithTransferUtility(key: String, filePath: String) {
        val options = TransferUtilityOptions()
        //스레드 수
        options.transferThreadPoolSize = 8
        //정지된 전송을 해당 주기로 감시 하여 발견시 실행 시는 시간
        options.transferServiceCheckTimeInterval = 2 * 60 * 1000 // 2-minutes

        val transferUtility = TransferUtility.builder()
            .context(AWSS3.context)
            .awsConfiguration(AWSMobileClient.getInstance().configuration)
            .s3Client(AmazonS3Client(AWSMobileClient.getInstance()))
            .transferUtilityOptions(options)
            .build()

        //TODO 경로 설정하고
        val path = filePath.replace(".jpg","hip.jpg")
        //리사이징 이미지 캐시 경로
        saveBitmapToFileCache(getBitmap(presenter?.getActivityContext()!!, filePath), path)
        val uploadObserver = transferUtility.upload(
            key,
            File(path)
        )

        // 관찰자에게 수신기를 연결하여 상태 업데이트 및 진행률 알림 가져옴
        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (TransferState.COMPLETED === state) {
                    // 업로드 완료
                    presenter?.tableDataSaveTransfer(state)
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
                presenter?.tableDataSaveTransfer(TransferState.FAILED)
            }
        })

        // 데이터를 폴링하려면 앨리스터를 부착하는 대신 관찰자의 상태 및 진행 상태를 확인하십시오.
        if (TransferState.COMPLETED === uploadObserver.state) {
            // 업로드 완료
            presenter?.tableDataSaveTransfer(uploadObserver.state)
        }

        Log.d("YourActivity", "Bytes Transferred: " + uploadObserver.bytesTransferred)
        Log.d("YourActivity", "Bytes Total: " + uploadObserver.bytesTotal)
    }

    private fun saveBitmapToFileCache(bitmap: Bitmap?, path: String){
        val fileCacheItem = File(path)
        var out: OutputStream? = null
        try {
            fileCacheItem.createNewFile()
            out = FileOutputStream(fileCacheItem)

            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }catch (e: Exception){}
        finally {
            try {
                out?.close()
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }
    private fun getBitmap(context: Context, path :String) : Bitmap? {
        var ins: InputStream?
        try {
            val IMAGE_MAX_SIZE = 1024 * 512 //1MB
            ins = context.contentResolver.openInputStream(Uri.fromFile(File(path)))

            // Decode image size
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(ins, null, options)
            ins.close()

            var scale = 1
            while ((options.outWidth * options.outHeight) * (1 / Math.pow(scale.toDouble(), 2.0)) >
                IMAGE_MAX_SIZE) {
                scale++
            }
            Log.d("TAG", "scale = " + scale + ", orig-width: " + options.outWidth + ", orig-height: " + options.outHeight)

            var resultBitmap: Bitmap?
            ins = context.contentResolver.openInputStream(Uri.fromFile(File(path)))
            if (scale > 1) {
                scale--
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                val option = BitmapFactory.Options()
                option.inSampleSize = scale
                resultBitmap = BitmapFactory.decodeStream(ins, null, option)

                // resize to desired dimensions
                val height = resultBitmap.height
                val width = resultBitmap.width
                Log.d("TAG", "1th scale operation dimension - width: $width, height: $height")

                val y = Math.sqrt(IMAGE_MAX_SIZE/ ((width.toDouble()) / height))
                val x = (y / height) * width

                val scaledBitmap = Bitmap.createScaledBitmap(resultBitmap, x.toInt(), y.toInt(), true)
                resultBitmap.recycle()
                resultBitmap = if(x>y){
                    //방향 맞춤
                    val matrix = Matrix()
                    matrix.postRotate(90F)
                    val scaledBitmap = Bitmap.createScaledBitmap(scaledBitmap, width, height, true)
                    Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
                }else{
                    scaledBitmap
                }


                System.gc()
            } else {
                resultBitmap = BitmapFactory.decodeStream(ins)
            }
            ins.close()

            Log.d("TAG", "bitmap size - width: " +resultBitmap.width + ", height: " + resultBitmap.height)
            return resultBitmap
        } catch (e: IOException) {
            Log.e("TAG", e.message, e)
            return null
        }
    }
}