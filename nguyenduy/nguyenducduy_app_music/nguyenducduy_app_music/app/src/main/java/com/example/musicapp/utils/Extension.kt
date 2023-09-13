package com.example.musicapp.utils

import android.media.MediaMetadataRetriever
import android.widget.ImageView
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.musicapp.MusicApplication
import com.squareup.picasso.Picasso

fun loadImage(image : ImageView,url : String?){
    url?.let {
        Glide.with(image)
            .load(it)
            .apply(RequestOptions().override(200, 200))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(image)
    }
}
fun loadImageWithSize(image : ImageView,url : String?,width : Int, height : Int){
    url?.let {
        Glide.with(image)
            .load(it)
            .apply(RequestOptions().override(width, height))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(image)
    }
}
fun loadImagePlayer(image : ImageView,url : String?){
    url?.let {
        Glide.with(image)
            .load(it)
            .apply(RequestOptions().override(250, 250))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(image)
    }
}
fun loadImageWithPlaceholder(res:Int,image: ImageView, url: String) {
    url.let {
        Glide.with(image)
            .load(it)
            .apply(RequestOptions().override(350, 350))
            .placeholder(res)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(image)
    }
}
fun loadImagePicasso(image: ImageView,url:String){
    Picasso.get().load(url).into(image)
}
fun getSongThumbnail(songPath: String): ByteArray? {
    var imgByte: ByteArray?
    MediaMetadataRetriever().also {
        try {
            it.setDataSource(songPath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        imgByte = it.embeddedPicture
        it.release()
    }
    return imgByte
}
fun String.toUpperString():String{
    return this.split(' ').joinToString(" ") { it.capitalize() }
}
fun getString(resource: Int) = MusicApplication.appContext.getString(resource)



