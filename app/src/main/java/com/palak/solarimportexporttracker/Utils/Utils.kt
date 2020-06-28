package com.palak.solarimportexporttracker.Utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.palak.solarimportexporttracker.R

class Utils {

    companion object{

        @JvmStatic
        @BindingAdapter("profileImage")
        fun loadImage(view: ImageView, imageUrl: String?) {
            if(imageUrl == null){
                view.setImageResource(android.R.color.transparent)
                view.setImageResource(R.drawable.shape_bg_profile)
                return
            }
            Glide.with(view.context)
                .load(imageUrl).apply(RequestOptions().circleCrop())
                .into(view)
        }
    }

}