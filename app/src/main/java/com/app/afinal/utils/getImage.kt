package com.app.afinal.utils

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso

class getImage {
    fun loadImage(context: Context, imageView: ImageView, imageUrl: String) {
        Picasso.get()
            .load(imageUrl)
            .into(imageView)
    }
}