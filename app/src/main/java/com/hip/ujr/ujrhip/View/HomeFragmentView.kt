package com.hip.ujr.ujrhip.View

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hip.ujr.ujrhip.R
import kotlinx.android.synthetic.main.fragment_home_fragment_view.view.*

class HomeFragmentView : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home_fragment_view, container, false)
        Glide.with(this)
            .load(resources.getDrawable(R.drawable.ujrhpi_img))
            .apply(RequestOptions().centerCrop())
            .into(view.mainImageView)
        view.mainImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragmentView().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
