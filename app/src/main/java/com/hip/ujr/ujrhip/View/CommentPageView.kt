package com.hip.ujr.ujrhip.View

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import com.hip.ujr.ujrhip.Adapter.CommentListAdapter
import com.hip.ujr.ujrhip.Contractor.CommentPageContractor
import com.hip.ujr.ujrhip.Etc.StateMaintainer
import com.hip.ujr.ujrhip.Etc.StringData
import com.hip.ujr.ujrhip.Etc.StringData.Companion.POSITION
import com.hip.ujr.ujrhip.Etc.StringData.Companion.POST_DATA
import com.hip.ujr.ujrhip.Etc.Util
import com.hip.ujr.ujrhip.Item.CommentData
import com.hip.ujr.ujrhip.Item.PostData
import com.hip.ujr.ujrhip.Model.CommentPageModel
import com.hip.ujr.ujrhip.Presenter.CommentPagePresenter
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.activity_comment_page_view.*

@Suppress("DEPRECATION")
class CommentPageView : AppCompatActivity(), CommentPageContractor.View {
    private val mStateMaintainer = StateMaintainer(fragmentManager, CommentPageView::class.java.name)
    private lateinit var presenter: CommentPagePresenter
    private lateinit var model: CommentPageModel
    private var sentBool = false
    var position: Int = 0
    lateinit var PostData: PostData
    private lateinit var commentAdapter: CommentListAdapter
    private val commentDataList = arrayListOf<CommentData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_page_view)

        initialize()
        setView()
        setMVP()
    }

    //댓글창 비어있음
    override fun commentIsEmpty(flag: Boolean) {
        sentBool = !flag
        if(flag)
            commentSendBtn.setTextColor(resources.getColor(R.color.disable))
        else
            commentSendBtn.setTextColor(resources.getColor(R.color.black))
    }
    //모델에서 가져온 데이터 세팅
    override fun dataLoad(commentDataList: ArrayList<CommentData>) {
        this.commentDataList.clear()
        this.commentDataList.addAll(commentDataList)
        commentAdapter.notifyDataSetChanged()
    }

    private fun initialize() {
        PostData = intent.getSerializableExtra(POST_DATA) as PostData
        position = intent.getIntExtra(POSITION,0)

        commentAdapter = CommentListAdapter(applicationContext, commentDataList)

//        for(i in 0 .. 9){
//            val text = CommentData()
//            text.type = 1
//            text.comment = "테스트입니다!@#!@#!@#$i"
//            text.id = System.currentTimeMillis()
//            text.userName = "테스트$i"
//            text.profileUrl = EMPTY
//            commentDataList.add(text)
//        }
    }
    private fun setView() {
        //백버튼
        backBtn.setOnClickListener { finish() }
        //리사이클러뷰 어뎁터 연결
        commentList.adapter = commentAdapter
        commentList.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        //댓글 등록 버튼
        commentSendBtn.setOnClickListener {
            if(sentBool){
                presenter.sendComment(commentEtx.text.toString())
                Util.isSoftKeyView(applicationContext,commentEtx, StringData.INVISIBLE)
            }
        }
        commentEtx.setOnKeyListener { v, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && sentBool) {
                //Enter키눌렀을떄 처리
                presenter.sendComment(commentEtx.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        //댓글 공백 체크
        commentEtx.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) { presenter.checkComment(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
    private fun setMVP() {
        if(mStateMaintainer.firstTimeIn()){
            val pre = CommentPagePresenter()
            val model = CommentPageModel()
            pre.setModel(model)
            pre.setView(this)
            model.setPresenter(pre)
            mStateMaintainer.put(pre)
            mStateMaintainer.put(model)

            presenter = pre
            this.model = model
        }else{
            presenter = mStateMaintainer.get(CommentPageView::class.java.name)
            presenter.setView(this)
        }
        presenter.getCommentList()
        Log.d("data_size","0000000")
    }
    override fun getAppContext(): Context = getAppContext()
    override fun getActivityContext(): Context = getActivityContext()
}
