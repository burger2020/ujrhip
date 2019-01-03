package com.hip.ujr.ujrhip.View

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.hip.ujr.ujrhip.Adapter.PostListAdapter
import com.hip.ujr.ujrhip.Dialog.ProfileDialog
import com.hip.ujr.ujrhip.Etc.AWSS3
import com.hip.ujr.ujrhip.Etc.Util
import com.hip.ujr.ujrhip.Item.TestClass
import com.hip.ujr.ujrhip.Item.UjrItem
import com.hip.ujr.ujrhip.R
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.orhanobut.dialogplus.DialogPlus
import kotlinx.android.synthetic.main.fragment_list_fragment_view.view.*
import java.util.*
import kotlin.concurrent.thread


class ListFragmentView : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private val REQUEST_IMAGE_CAPTURE = 10942

    private lateinit var rootView : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_list_fragment_view, container, false)
        val ujrItem = ArrayList<UjrItem>()
        val c = Calendar.getInstance()
        for(i in 0 until 5)
        ujrItem.add(UjrItem(
            "",
            "위치는 을지로가역 부근\n을지로",
            "을지로",
            c.timeInMillis,
            "aa"))

        val postListAdapter = PostListAdapter(context!!,ujrItem)
        rootView.postListView.adapter = postListAdapter
        rootView.postListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        initialize()
        setView()
        setMVP()

        createNews()

        return rootView
    }

    private fun createNews() {
        val newsItem = TestClass()
        newsItem.userId = "unique-user-id"
        newsItem.date = System.currentTimeMillis()
        newsItem.content = "This is the article content"

        thread(start = true) {
            dynamoDBMapper?.save(newsItem)
        }
    }
    //사진 선택 옵션 다이얼로그
    private fun addPhoto() {
        val adapter = ProfileDialog(rootView.context, 3)
        var overlapClick = true
        val dialog = DialogPlus.newDialog(rootView.context)
            .setAdapter(adapter)
            .setExpanded(false, 600)
            .setOnItemClickListener { dialog, _, _, position ->
                if(overlapClick) {
                    overlapClick = false
                    profileImageSetting(position)
                    dialog.dismiss()
                    overlapClick = true
                }
            }
            .create()
        dialog.show()
    }
    //사진 선택 옵션
    private fun profileImageSetting(sect: Int) {
        when (sect) {
            1 -> { //사진 찍어 변경
                Util.imageCapture(this,REQUEST_IMAGE_CAPTURE)
            }
            2 -> { //갤러리에서 변경
                Util.gallerySelect(this)
            }
        }
    }
    //이미지 서버에 저장
    private fun profileImgUpload(path: String){
        AWSS3.uploadWithTransferUtility(rootView.context,System.currentTimeMillis().toString(), path)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == AppCompatActivity.RESULT_OK && data != null) { //갤러리에서 사진선택
            val images = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)[0].path
            if(images[images.length-1]!='f'){ //gif아닌지 확인
//                userProfileImageView.isDrawingCacheEnabled = true
//                userProfileImageView.buildDrawingCache()
                val bitmap = BitmapFactory.decodeFile(images)//경로를 통해 비트맵으로 전환
                profileImgUpload(images)
            }else{ Toast.makeText(rootView.context,"gif", Toast.LENGTH_SHORT).show() }
        }else if(requestCode == REQUEST_IMAGE_CAPTURE&& resultCode == AppCompatActivity.RESULT_OK){
            val extras = data!!.extras
            val imageBitmap = extras.get("data")
            profileImgUpload(extras.get("data").toString())
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private var dynamoDBMapper : DynamoDBMapper? = null
    //    기본 세팅 초기화
    private fun initialize() {
        val client = AmazonDynamoDBClient(AWSMobileClient.getInstance().credentialsProvider)
        dynamoDBMapper = DynamoDBMapper.builder()
            .dynamoDBClient(client)
            .awsConfiguration(AWSMobileClient.getInstance().configuration)
            .build()
    }
//    뷰 초기화
    private fun setView() {
    //사진 선택
        rootView.createPost.setOnClickListener { addPhoto() }
    }
//mvp 초기화
    private fun setMVP() {

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ListFragmentView().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
