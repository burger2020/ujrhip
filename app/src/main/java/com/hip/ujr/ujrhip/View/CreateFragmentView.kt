package com.hip.ujr.ujrhip.View

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table
import com.hip.ujr.ujrhip.Contractor.CreateFragmentContractor
import com.hip.ujr.ujrhip.Dialog.ProfileDialog
import com.hip.ujr.ujrhip.Etc.AWSDB
import com.hip.ujr.ujrhip.Etc.AWSS3
import com.hip.ujr.ujrhip.Etc.Util
import com.hip.ujr.ujrhip.Item.postData
import com.hip.ujr.ujrhip.R
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.orhanobut.dialogplus.DialogPlus
import kotlinx.android.synthetic.main.fragment_create_fragment_view.*
import kotlinx.android.synthetic.main.fragment_create_fragment_view.view.*
import java.util.*
import kotlin.concurrent.thread


class CreateFragmentView : Fragment(), View.OnClickListener, CreateFragmentContractor.View {
    private val REQUEST_IMAGE_CAPTURE = 10942


    private var dbTable : Table? = null


    var content = ""
    var writer = ""
    var password = ""
    var photoUrl = "nonUrl"

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateFragmentView().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    lateinit var root : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_create_fragment_view, container, false)

        initialize()
        setView(root)

        return root
    }

    //클릭 리스너
    override fun onClick(v: View?) {
        when(v){
            successBtn->{   //등록버튼
                Toast.makeText(context,"등록",Toast.LENGTH_SHORT).show()
                val c = Calendar.getInstance()
                val data = postData(writer,c.timeInMillis,password,photoUrl,content)

                thread(start = true) {
                    AWSDB.createTable(data)
                }
                profileImgUpload(data.photoUrl!!)
            }
            backBtn->{      //백버튼

            }
            postPhoto->{    //사진 선택
                addPhoto()
            }
        }
    }
    //사진 선택 옵션 다이얼로그
    private fun addPhoto() {
        val adapter = ProfileDialog(root.context, 3)
        var overlapClick = true
        val dialog = DialogPlus.newDialog(root.context)
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
        AWSS3.uploadWithTransferUtility(System.currentTimeMillis().toString(), path)
    }
    private fun initialize(){
    }
    //뷰 초기화
    private fun setView(view: View) {
        val watcher = object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                // 내용 10자, 게시자명 3자, 게시물 비밀번호 4자 이상 입력시 등록버튼 활성화
                content = postContent.text.toString()
                writer = postWriter.text.toString()
                password = postPassword.text.toString()
                if(writer.length>=3 &&  password.length>=4 && content.length>=10) {
                    successBtn.setTextColor(resources.getColor(R.color.black))
                    successBtn.isEnabled = true
                }
                else {
                    successBtn.setTextColor(resources.getColor(R.color.enable))
                    successBtn.isEnabled = false
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        //등록버튼 클릭 리스너
        view.successBtn.setOnClickListener(this)
        view.postPhoto.setOnClickListener(this)
        //게시자 명 입력 리스너
        view.postWriter.addTextChangedListener(watcher)
        //게시물 비밀번호 입력 리스너
        view.postPassword.addTextChangedListener(watcher)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == AppCompatActivity.RESULT_OK && data != null) { //갤러리에서 사진선택
            val images = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)[0].path
            //gif아닌지 확인
            if(images[images.length-1]!='f'){
                photoUrl = images
            }
            else{ Toast.makeText(root.context,"gif", Toast.LENGTH_SHORT).show() }
        }
        else if(requestCode == REQUEST_IMAGE_CAPTURE&& resultCode == AppCompatActivity.RESULT_OK){
            val extras = data!!.extras
            photoUrl = extras.get("data").toString()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
