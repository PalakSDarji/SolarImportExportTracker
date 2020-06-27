package com.palak.solarimportexporttracker.Utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class Utils {

    companion object{

        @JvmStatic
        @BindingAdapter("profileImage")
        fun loadImage(view: ImageView, imageUrl: String?) {
            if(imageUrl == null){
                view.setImageResource(android.R.color.transparent)
                return
            }
            Glide.with(view.context)
                .load(imageUrl).apply(RequestOptions().circleCrop())
                .into(view)
        }
    }

}