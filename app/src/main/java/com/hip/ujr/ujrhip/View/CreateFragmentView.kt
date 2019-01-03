package com.hip.ujr.ujrhip.View

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hip.ujr.ujrhip.Contractor.CreateFragmentContractor
import com.hip.ujr.ujrhip.Item.UjrItem
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.fragment_create_fragment_view.*
import kotlinx.android.synthetic.main.fragment_create_fragment_view.view.*
import java.util.*


class CreateFragmentView : Fragment(), View.OnClickListener, CreateFragmentContractor.View {
    companion object {
        @JvmStatic
        fun newInstance() =
            CreateFragmentView().apply {
                arguments = Bundle().apply {
                }
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_fragment_view, container, false)
        setView(view)
        return view
    }
    var content = ""
    var writer = ""
    var password = ""
    var photoUrl = ""
    private fun setView(view: View) {
        val watcher = object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                // 내용 10자, 게시자명 3자, 게시물 비밀번호 4자 이상 입력시 등록버튼 활성화
                content = postContent.text.toString()
                writer = postWriter.text.toString()
                password = postPassword.text.toString()
                if(writer.length>=3 &&  password.length>=4 && content.length>=10) {
                    successBtn.setTextColor(resources.getColor(R.color.black))
                    successBtn.isEnabled = true
                }
                else {
                    successBtn.setTextColor(resources.getColor(R.color.enable))
                    successBtn.isEnabled = false
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        //등록버튼 클릭 리스너
        view.successBtn.setOnClickListener(this)
        view.postPhoto.setOnClickListener(this)
        //게시자 명 입력 리스너
        view.postWriter.addTextChangedListener(watcher)
        //게시물 비밀번호 입력 리스너
        view.postPassword.addTextChangedListener(watcher)
    }
    //클릭 리스너
    override fun onClick(v: View?) {
        when(v){
            successBtn->{   //등록버튼
                Toast.makeText(context,"등록",Toast.LENGTH_SHORT).show()
                val c = Calendar.getInstance()
                val data = UjrItem(photoUrl,content,writer,c.timeInMillis,password)
            }
            backBtn->{      //백버튼

            }
            postPhoto->{    //사진 선택

            }
        }
    }
}
