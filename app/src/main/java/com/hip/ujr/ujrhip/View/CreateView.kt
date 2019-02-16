package com.hip.ujr.ujrhip.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState.COMPLETED
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState.FAILED
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hip.ujr.ujrhip.Contractor.CreateContractor
import com.hip.ujr.ujrhip.Dialog.ProfileDialog
import com.hip.ujr.ujrhip.Etc.AWSCognito
import com.hip.ujr.ujrhip.Etc.AWSS3
import com.hip.ujr.ujrhip.Etc.StateMaintainer
import com.hip.ujr.ujrhip.Etc.StringData.Companion.INVISIBLE
import com.hip.ujr.ujrhip.Etc.StringData.Companion.POST_TYPE
import com.hip.ujr.ujrhip.Etc.StringData.Companion.UPLOAD_COMPLETED
import com.hip.ujr.ujrhip.Etc.StringData.Companion.VISIBLE
import com.hip.ujr.ujrhip.Etc.Util
import com.hip.ujr.ujrhip.Item.PostData
import com.hip.ujr.ujrhip.Model.CreateModel
import com.hip.ujr.ujrhip.Presenter.CreatePresenter
import com.hip.ujr.ujrhip.R
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.orhanobut.dialogplus.DialogPlus
import kotlinx.android.synthetic.main.activity_create_view.*

class CreateView : AppCompatActivity(), CreateContractor.View {
    private val mStateMaintainer = StateMaintainer(fragmentManager, CreateView::class.java.name)
    private lateinit var model: CreateModel
    private lateinit var presenter: CreatePresenter
    private val REQUEST_IMAGE_CAPTURE = 10942
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
        Util.isSoftKeyView(this,postContentTxt, INVISIBLE)
        val menuName =
            arrayListOf(getString(R.string.profileDialogText),
                getString(R.string.profileDialogText1),
                getString(R.string.profileDialogText2))
        val adapter = ProfileDialog(this, menuName)
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
    //게시물 등록
    private fun pushData() {
        Util.isSoftKeyView(this,postContentTxt, INVISIBLE)
        progressbarVisible(VISIBLE)
        //TODO 유저 접속상태 확인하고 저장 데이터 구조 정하기
//        if(AWSCognito.currUser)
        val postData = PostData(POST_TYPE, System.currentTimeMillis(), postPasswordTxt.text.toString(), userIdTxt.text.toString(), postContentTxt.text.toString(), System.currentTimeMillis().toString())
        presenter.addPhoto(postData, path)
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
        //갤러리에서 사진선택
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            path = data.getParcelableArrayListExtra<Image>(Config.EXTRA_IMAGES)[0].path
            //gif아닌지 확인
            setImageView(path)
//            if(images[images.length-1]!='f')
//            else Toast.makeText(applicationContext,"gif", Toast.LENGTH_SHORT).show()
        }
        //사진 찍기
        else if(requestCode == REQUEST_IMAGE_CAPTURE&& resultCode == AppCompatActivity.RESULT_OK){
            val extras = data!!.extras
            path = extras.get("data").toString()
            setImageView(path)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    //사진 선택시 이미지뷰 사진 적용
    private fun setImageView(path: String){
        Glide.with(this)
            .load(path)
            .apply(RequestOptions().centerCrop())
            .into(photoImg)
    }
    //이미지 로드 완료시 콜백
    override fun tableDataSaveTransfer(state: TransferState) {
        when(state){
            COMPLETED->{
                uploadSuccess()
            }
            FAILED ->{
                Snackbar.make(window.decorView.rootView,"다시시도해 주세요.",Snackbar.LENGTH_SHORT).show()
                progressbarVisible(INVISIBLE)
            }
            else -> Log.d("save_transfer: ", state.toString())
        }
    }
    //업로드 완료 및 페이지 나가기
    override fun uploadSuccess(){
        setResult(UPLOAD_COMPLETED)
        finish()
    }
    //초기화
    private fun initialize(){
        AWSS3.init(this)
    }
    //뷰
    private fun setView() {
        val watcher = object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 내용 10자, 게시자명 3자, 게시물 비밀번호 4자 이상 입력시 등록버튼 활성화
                if(presenter.checkForm(postContentTxt.text.toString(), userIdTxt.text.toString(), postPasswordTxt.text.toString())){
                    postPushBtn.setTextColor(resources.getColor(R.color.black))
                    postPushBtn.isEnabled = true
                }else {
                    postPushBtn.setTextColor(resources.getColor(R.color.disable))
                    postPushBtn.isEnabled = false
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        //등록버튼 클릭 리스너
        postPushBtn.setOnClickListener{ pushData() }
        //사진 선택
        photoImg.setOnClickListener{ addPhoto() }
        //게시글 입력 리스너
        postContentTxt.addTextChangedListener(watcher)
        //게시자 명 입력 리스너
        userIdTxt.addTextChangedListener(watcher)
        //게시물 비밀번호 입력 리스너
        postPasswordTxt.addTextChangedListener(watcher)
    }
    //MVP
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
    override fun progressbarVisible(visible: Int) {
        when(visible){
            VISIBLE->{
                progressBar.visibility = View.VISIBLE
                listProgress.start()
            }
            INVISIBLE->{
                progressBar.visibility = View.GONE
                listProgress.stop()
            }
        }
    }
}
