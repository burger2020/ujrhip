package com.hip.ujr.ujrhip.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hip.ujr.ujrhip.Etc.StringData
import com.hip.ujr.ujrhip.Etc.StringData.Companion.TYPE_FOOTER
import com.hip.ujr.ujrhip.Etc.StringData.Companion.TYPE_HEADER
import com.hip.ujr.ujrhip.Item.CommentData
import java.util.*

class CommentListAdapter(val context: Context,private val commentList: ArrayList<CommentData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int = commentList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(viewType){
            TYPE_FOOTER->{
                val mainView = inflater.inflate(com.hip.ujr.ujrhip.R.layout.adapter_list_ujr_footer,parent,false)
                PostListAdapter.ViewHolderFooter(mainView)
            }
            TYPE_HEADER->{
                val mainView = inflater.inflate(com.hip.ujr.ujrhip.R.layout.adapter_list_ujr,parent,false)
                PostListAdapter.ViewHolder(mainView)
            }
            else->{
                val mainView = inflater.inflate(com.hip.ujr.ujrhip.R.layout.adapter_list_ujr,parent,false)
                PostListAdapter.ViewHolder(mainView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (position) {
//            0                -> TYPE_HEADER
            commentList.size -> TYPE_FOOTER
            else             -> StringData.TYPE_ITEM
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CommentListViewHolder -> if(commentList.isNotEmpty()) holder.onBind(context, commentList[position - 1], position)
            is CommentListViewHolderHeader -> holder.onBind()
            else -> {

            }
        }
    }
    inner class CommentListViewHolderHeader(view: View):RecyclerView.ViewHolder(view){
        fun onBind(){

        }
    }
    inner class CommentListViewHolder(view: View):RecyclerView.ViewHolder(view){
        fun onBind(context: Context, commentList: CommentData, position: Int){

        }
    }
}
