package com.hip.ujr.ujrhip.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hip.ujr.ujrhip.Contractor.ListFragmentContractor
import com.hip.ujr.ujrhip.Etc.StringData.Companion.TYPE_FOOTER
import com.hip.ujr.ujrhip.Etc.StringData.Companion.TYPE_HEADER
import com.hip.ujr.ujrhip.Etc.StringData.Companion.TYPE_ITEM
import com.hip.ujr.ujrhip.Item.PostData
import com.hip.ujr.ujrhip.R
import com.instacart.library.truetime.TrueTimeRx
import kotlinx.android.synthetic.main.adapter_list_ujr.view.*
import java.util.*

class PostListAdapter(val context: Context, private val ujrItem: List<PostData>, private val callBack: ListFragmentContractor.View): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    //아이템만 있으면 그대로 / 푸터있으면 +1 / 푸터,헤더 둘다있으면 +2
    override fun getItemCount(): Int = ujrItem.size + 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(viewType){
            TYPE_FOOTER->{
                val mainView = inflater.inflate(com.hip.ujr.ujrhip.R.layout.adapter_list_ujr_footer,parent,false)
                ViewHolderFooter(mainView)
            }
            TYPE_HEADER->{
                val mainView = inflater.inflate(com.hip.ujr.ujrhip.R.layout.adapter_comment_list,parent,false)
                ViewHolder(mainView)
            }
            else->{
                val mainView = inflater.inflate(com.hip.ujr.ujrhip.R.layout.adapter_list_ujr,parent,false)
                ViewHolder(mainView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
//            0                -> TYPE_HEADER
            ujrItem.size -> TYPE_FOOTER
            else             -> TYPE_ITEM
        }
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> if(ujrItem.isNotEmpty()) holder.onBind(context, ujrItem[position], position, callBack)
            is ViewHolderFooter -> holder.onBind()
            else -> {

            }
        }
    }
    //바디
    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        private val photo = view.postPhoto
        private val content = view.postContent
        private val write = view.postWriter
        private val date = view.postDate
        @SuppressLint("SetTextI18n")
        fun onBind(context: Context, ujrItem: PostData, position: Int, callBack: ListFragmentContractor.View) {
            val path = "${context.getString(R.string.aws_s3_phath)}${ujrItem.imageUrl}"
            //등록 사진
//            if(ujrItem[position].imageUrl != EMPTY)
            Glide.with(context)
                .load(path)
                .apply(RequestOptions().centerInside())
                .into(photo)
//            photo.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            //내용
            content.text = ujrItem.content
            //작성자
            write.text = "${ujrItem.userId}"
            //네트워크 시간
            val trueTime = try {
                TrueTimeRx.now().time
            }catch (e : IllegalStateException){
                System.currentTimeMillis()
            }
            //날짜
            val postTime = Calendar.getInstance()
            postTime.timeInMillis = ujrItem.date!!
            val nowTime = Calendar.getInstance()
            nowTime.timeInMillis = trueTime
            val difDate = Math.abs(postTime[Calendar.DATE] - nowTime[Calendar.DATE])
            when {
                Math.abs(postTime[Calendar.YEAR] - nowTime[Calendar.YEAR]) >= 1 -> "${postTime.get(Calendar.YEAR)}. ${postTime.get(Calendar.MONTH) + 1}. ${postTime.get(Calendar.DAY_OF_MONTH)}"
                difDate > 10 -> date.text = "${postTime.get(Calendar.MONTH) + 1}월 ${postTime.get(Calendar.DAY_OF_MONTH)}일"
                difDate == 0 -> date.text = "${Math.abs(postTime[Calendar.HOUR] - nowTime[Calendar.HOUR])}시간전"
                difDate < 10 -> date.text = "${difDate}일전"
            }
            //옵션버튼 눌렀을때
            view.postOptionBtn.setOnClickListener { callBack.listOptionClick(ujrItem, position) }
            //좋아요
            view.likeBtn.setOnClickListener { callBack.likeBtnClick(ujrItem, position) }
            view.likeNumTxt.setOnClickListener { callBack.likeBtnClick(ujrItem, position) }
            //댓글
            view.commentBtn.setOnClickListener { callBack.commentBtnClick(ujrItem, position) }
            view.commentNumTxt.setOnClickListener { callBack.commentBtnClick(ujrItem, position) }
        }
    }
    //푸터
    class ViewHolderFooter(view: View):RecyclerView.ViewHolder(view){
        fun onBind(){

        }
    }
}