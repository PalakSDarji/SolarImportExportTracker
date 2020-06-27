package com.palak.solarimportexporttracker.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


data class User(val uid : String, val name : String?, val email : String?, val providerId : String,
    val profileImage : String?)