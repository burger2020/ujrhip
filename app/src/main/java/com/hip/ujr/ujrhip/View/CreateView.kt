package com.hip.ujr.ujrhip.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hip.ujr.ujrhip.Contractor.CreateContractor
import com.hip.ujr.ujrhip.Dialog.ProfileDialog
import com.hip.ujr.ujrhip.Etc.AWSDB
import com.hip.ujr.ujrhip.Etc.AWSS3
import com.hip.ujr.ujrhip.Etc.StateMaintainer
import com.hip.ujr.ujrhip.Etc.Util
import com.hip.ujr.ujrhip.Model.CreateModel
import com.hip.ujr.ujrhip.Presenter.CreatePresenter
import com.hip.ujr.ujrhip.R
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.orhanobut.dialogplus.DialogPlus
import kotlinx.android.synthetic.main.activity_create_view.*
import java.util.*
import kotlin.concurrent.thread

class CreateView : AppCompatActivity(), CreateContractor.View {
    private val mStateMaintainer = StateMaintainer(fragmentManager, CreateView::class.java.name)
    private lateinit var model: CreateModel
    private lateinit var presenter: CreatePresenter

    private val REQUEST_IMAGE_CAPTURE = 10942
//    private var dbTable : Table? = null
    var content = ""
    var writer = ""
    var password = ""
    var photoUrl = "nonUrl"
    var path = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_view)

        initialize()
        setView()
        setMVP()
    }
    //사진 선택 옵션 다이얼로그
    private fun addPhoto() {
        val adapter = ProfileDialog(this, 3)
        var overlapClick = true
        val dialog = DialogPlus.newDialog(this)
            .setAdapter(adapter)
            .setExpanded(false, 600)
            .setOnItemClickListener { dialog, _, _, position ->
                if(overlapClick) {
                    overlapClick = false
                    profileImageSetting(position)
                    dialog.dismiss()
                    overlapClick = true
                }
            }.create()
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == AppCompatActivity.RESULT_OK && data != null) { //갤러리에서 사진선택
            val images = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)[0].path
            //gif아닌지 확인
            if(images[images.length-1]!='f'){
                path = images
                Glide.with(this)
                    .load(path)
                    .apply(RequestOptions().centerCrop())
                    .into(photoImg)
            }
            else{ Toast.makeText(applicationContext,"gif", Toast.LENGTH_SHORT).show() }
        }
        else if(requestCode == REQUEST_IMAGE_CAPTURE&& resultCode == AppCompatActivity.RESULT_OK){
            val extras = data!!.extras
            path = extras.get("data").toString()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initialize(){
        AWSS3.init(this)
    }
    private fun setView() {
        val watcher = object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 내용 10자, 게시자명 3자, 게시물 비밀번호 4자 이상 입력시 등록버튼 활성화
                content = postContentTxt.text.toString()
                writer = userIdTxt.text.toString()
                password = postPasswordTxt.text.toString()
                if(writer.length>=3 &&  password.length>=4 && content.length>=10) {
                    postPushBtn.setTextColor(resources.getColor(R.color.black))
                    postPushBtn.isEnabled = true
                }
                else {
                    postPushBtn.setTextColor(resources.getColor(R.color.enable))
                    postPushBtn.isEnabled = false
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        //등록버튼 클릭 리스너
        postPushBtn.setOnClickListener{
            Toast.makeText(applicationContext,"등록",Toast.LENGTH_SHORT).show()
            val c = Calendar.getInstance()
            photoUrl = System.currentTimeMillis().toString()
            model.setData(writer,c.timeInMillis,password,photoUrl,content)
            model.savePhoto(photoUrl, path)

            thread(start = true) {
                AWSDB.createTable(model.data)
            }
        }
        //사진 선택
        photoImg.setOnClickListener{
            addPhoto()
        }
        //게시글 입력 리스너
        postContentTxt.addTextChangedListener(watcher)
        //게시자 명 입력 리스너
        userIdTxt.addTextChangedListener(watcher)
        //게시물 비밀번호 입력 리스너
        postPasswordTxt.addTextChangedListener(watcher)
    }
    private fun setMVP(){
        if(mStateMaintainer.firstTimeIn()){
            val pre = CreatePresenter()
            val model = CreateModel()
            pre.setModel(model)
            pre.setView(this)
            model.setPresenter(pre)
            mStateMaintainer.put(pre)
            mStateMaintainer.put(model)

            presenter = pre
            this.model = model
        }else{
            presenter = mStateMaintainer.get(CreateView::class.java.name)
            presenter.setView(this)
        }
    }
    override fun getAppContext(): Context = applicationContext
    override fun getActivityContext(): Context = this
}