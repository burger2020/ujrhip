package com.hip.ujr.ujrhip.Adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
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
        val photo = view.postPhoto
        val content = view.postContent
        val write = view.postWriter
        val date = view.postDate
        fun onBind(context: Context, ujrItem: ArrayList<UjrItem>, position: Int){
            //사진
            Glide.with(context)
                .load(
                    BitmapFactory.decodeResource(context.resources, R.drawable.ujrhpi_img))
                .into(photo)
            //내용
            content.text = ujrItem[position].content
            //작성자
            write.text = ujrItem[position].writer
            //날짜
            val c = Calendar.getInstance()
            c.timeInMillis = ujrItem[position].date
            date.text = "${c.get(Calendar.YEAR)}. ${c.get(Calendar.MONTH) + 1}. ${c.get(Calendar.DAY_OF_MONTH)}"
        }
    }
}