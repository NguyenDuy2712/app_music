package com.example.musicapp.config

import android.provider.MediaStore
import com.example.musicapp.manager.PlayerManager

object Config {
    //Api
    fun endPointApi() : String {
        return getEndPointApi()
    }
    @Suppress("DEPRECATION")
    val baseProjection = arrayOf(
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.AudioColumns.TITLE,
        MediaStore.Audio.ArtistColumns.ARTIST,
        MediaStore.Audio.AudioColumns.DATA
        )
   private fun getEndPointApi():String{
        return when(PlayerManager.api){
            "top100","KoreaMusic","EnglishSong","V-POP"->{
                "https://621f24a1311a70591401d374.mockapi.io/"
            }
            "TiktokMusic","BXH","Artist","RapViet" ->{
                "https://62826fd79fac04c6541522dd.mockapi.io/"
            }
            else ->{
                "https://6283c65838279cef71dc29e9.mockapi.io/"
            }
        }
    }
}