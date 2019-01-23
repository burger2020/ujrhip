package com.hip.ujr.ujrhip.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hip.ujr.ujrhip.Item.postData
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.adapter_list_ujr.view.*
import java.util.*

class PostListAdapter(val context: Context, private val ujrItem: List<postData>): RecyclerView.Adapter<PostListAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostListAdapter.ViewHolder {
        val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val mainView = inflater.inflate(R.layout.adapter_list_ujr,parent,false)
        return ViewHolder(mainView)
    }
    override fun getItemCount(): Int = ujrItem.size
    override fun onBindViewHolder(holder: PostListAdapter.ViewHolder, position: Int) = holder.onBind(context, ujrItem, position)

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val photo = view.postPhoto
        private val content = view.postContent
        private val write = view.postWriter
        private val date = view.postDate
        @SuppressLint("SetTextI18n")
        fun onBind(context: Context, ujrItem: List<postData>, position: Int){
            val path = "https://s3-ap-northeast-1.amazonaws.com/ujrhip727dc1caafc14cabbd4f1379cb5fc041/${ujrItem[position].imageUrl}"
            //등록 사진
//            if(ujrItem[position].imageUrl != EMPTY)
            Glide.with(context)
                .load(path)
                .apply(RequestOptions().centerInside())
                .thumbnail(0f)
                .into(photo)
//            photo.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            //내용
            content.text = ujrItem[position].content
            //작성자
            write.text = "작성자: ${ujrItem[position].userId}"
            //날짜
            val c = Calendar.getInstance()
            c.timeInMillis = ujrItem[position].date!!
            date.text = "${c.get(Calendar.YEAR)}. ${c.get(Calendar.MONTH) + 1}. ${c.get(Calendar.DAY_OF_MONTH)}"
        }
    }
}