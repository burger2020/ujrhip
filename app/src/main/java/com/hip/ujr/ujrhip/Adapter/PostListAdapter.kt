package com.hip.ujr.ujrhip.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hip.ujr.ujrhip.Item.postData
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.adapter_list_ujr.view.*
import java.util.*

class PostListAdapter(val context: Context, private val ujrItem: ArrayList<postData>): RecyclerView.Adapter<PostListAdapter.ViewHolder>(){
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
        fun onBind(context: Context, ujrItem: ArrayList<postData>, position: Int){
            //사진
//            Glide.with(context)
//                .load(R.drawable.test_img)
//                .apply(RequestOptions().centerCrop())
//                .into(photo)
            photo.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            Glide.with(context)
                .load("https://s3-ap-northeast-1.amazonaws.com/ujrhipa71b47235b024d62acb16e6fab62bfad/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7+2018-12-29+%EC%98%A4%ED%9B%84+5.16.49.png")
                .apply(RequestOptions().centerInside())
                .into(photo)
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