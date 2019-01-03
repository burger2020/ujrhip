package com.hip.ujr.ujrhip.Adapter

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.hip.ujr.ujrhip.Item.UjrItem
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.adapter_list_ujr.view.*
import java.util.*

class PostListAdapter(val context: Context, private val ujrItem: ArrayList<UjrItem>): RecyclerView.Adapter<PostListAdapter.ViewHolder>(){
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
        fun onBind(context: Context, ujrItem: ArrayList<UjrItem>, position: Int){
            //사진
//            Glide.with(context)
//                .load(R.drawable.test_img)
//                .apply(RequestOptions().centerCrop())
//                .into(photo)
            photo.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            Glide.with(context)
                .load("https://a2.muscache.com/im/pictures/9c66dcc5-6d39-43b3-82ec-72e9d119f873.jpg")
                .into(photo)
            //내용
            content.text = ujrItem[position].content
            //작성자
            write.text = "작성자: ${ujrItem[position].writer}"
            //날짜
            val c = Calendar.getInstance()
            c.timeInMillis = ujrItem[position].date
            date.text = "${c.get(Calendar.YEAR)}. ${c.get(Calendar.MONTH) + 1}. ${c.get(Calendar.DAY_OF_MONTH)}"
        }
    }
}