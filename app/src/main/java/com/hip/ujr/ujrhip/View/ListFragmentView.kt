package com.hip.ujr.ujrhip.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hip.ujr.ujrhip.Adapter.PostListAdapter
import com.hip.ujr.ujrhip.Contractor.ListFragmentContractor
import com.hip.ujr.ujrhip.Dialog.ProfileDialog
import com.hip.ujr.ujrhip.Etc.AWSDB
import com.hip.ujr.ujrhip.Etc.AWSDBCallback
import com.hip.ujr.ujrhip.Etc.AWSS3
import com.hip.ujr.ujrhip.Etc.StringData.Companion.CREATE_ACTIVITY
import com.hip.ujr.ujrhip.Etc.StringData.Companion.POSITION
import com.hip.ujr.ujrhip.Etc.StringData.Companion.POST
import com.hip.ujr.ujrhip.Etc.StringData.Companion.POST_DATA
import com.hip.ujr.ujrhip.Etc.StringData.Companion.UPLOAD_COMPLETED
import com.hip.ujr.ujrhip.Item.PostData
import com.hip.ujr.ujrhip.R
import com.orhanobut.dialogplus.DialogPlus
import kotlinx.android.synthetic.main.fragment_list_fragment_view.view.*


@Suppress("CAST_NEVER_SUCCEEDS", "UNCHECKED_CAST")
class ListFragmentView : Fragment(), AWSDBCallback, ListFragmentContractor.View {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    private lateinit var rootView : View

    private var postDataList : ArrayList<PostData> = arrayListOf()
    private lateinit var postListAdapter : PostListAdapter

    private val handlerThread = Handler()

    private var addListFlag = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_list_fragment_view, container, false)

        initialize()
        setView()
        setMVP()

        return rootView
    }
    //리사이클러뷰 스크롤 막기
    private fun listScroll(flag: Boolean){
        if(!flag)
            rootView.postListView.setOnTouchListener(object : View.OnTouchListener{
                @SuppressLint("ClickableViewAccessibility")
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    return true
                }
            })
        else
            rootView.postListView.setOnTouchListener(null)

    }
    //인터페이스 콜백  리스트 새로고침/초기화
    override fun <T>loadDataCallback(data: ArrayList<T>) {
        postDataList.clear()
        postDataList.addAll(data as ArrayList<PostData>)
        handlerThread.post {
            postListAdapter.notifyDataSetChanged()
            rootView.refreshLayout.isRefreshing = false

        }
        listScroll(true)
    }
    //리스트 옵션 버튼 클릭
    override fun listOptionClick(postData: PostData, position: Int) {
        val menuName =
            arrayListOf(getString(R.string.optionDialogText), getString(R.string.optionDialogText1), getString(R.string.optionDialogText2))
        val adapter = ProfileDialog(rootView.context, menuName)
        var overlapClick = true
        val dialog = DialogPlus.newDialog(rootView.context)
            .setAdapter(adapter)
            .setExpanded(false, 600)
            .setOnItemClickListener { dialog, _, _, item ->
                if(overlapClick) {
                    overlapClick = false
                    when(item){
                        1->{
                            AWSDB.deleteList(postData)
                            postDataList.remove(postData)
                            Toast.makeText(context,"삭제 되었습니다.",Toast.LENGTH_SHORT).show()
                            postListAdapter.notifyItemRemoved(position)
                        }
                        2->{
                            Toast.makeText(context,"신고",Toast.LENGTH_SHORT).show()
                        }
                    }
                    dialog.dismiss()
                    overlapClick = true
                }
            }
            .create()
        dialog.show()
    }
    //좋아요 클릭
    override fun likeBtnClick(PostData: PostData, position: Int) {
    }
    //댓글 클릭 -> 댓글창 이동
    override fun commentBtnClick(PostData: PostData, position: Int) {
        val intent = Intent(context,CommentPageView::class.java)
        intent.putExtra(POST_DATA, PostData)
        intent.putExtra(POSITION, position)
        startActivity(intent)
    }
    //인터페이스 콜백 리스트 추가
    override fun <T>addDataCallback(data: ArrayList<T>) {
        postDataList.addAll(data as ArrayList<PostData>)
        handlerThread.post {
            postListAdapter.notifyDataSetChanged()
            rootView.refreshLayout.isRefreshing = false
            addListFlag = true
        }
        listScroll(true)
    }
    //리스트 새로고침
    fun refreshData(){
        AWSDB.getList(this, POST)
        listScroll(false)
    }
    //기본 세팅 초기화
    private fun initialize() {
        postListAdapter = PostListAdapter(rootView.context,postDataList, this)
        rootView.postListView.adapter = postListAdapter
        rootView.postListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        AWSDB.getList(this, POST)
    }
    //하단 내릴시 리스트 추가
    private fun addList(){
//        if(addListFlag && postDataList.size!=0 && postDataList[postDataList.size - 1].type!! > 1) {
//            addListFlag = false
//            listScroll(false)
//            AWSDB.addList(this, postDataList[postDataList.size - 1].type!!)
//        }
    }
    //뷰 초기화
    private fun setView() {
        rootView.refreshLayout.setOnRefreshListener { refreshData() }

        rootView.postListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = rootView.postListView.adapter?.itemCount?.minus(1)
                if (lastVisibleItemPosition == itemTotalCount) {
                    addList()
                }
            }
        })
    }
    //mvp 초기화
    private fun setMVP() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CREATE_ACTIVITY->{
                when(resultCode){
                    //업로드 성공 후 돌아오면 게시물 초기화
                    UPLOAD_COMPLETED->{
                        refreshData()
                        Snackbar.make(rootView,"게시물이 등록되었습니다.", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
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