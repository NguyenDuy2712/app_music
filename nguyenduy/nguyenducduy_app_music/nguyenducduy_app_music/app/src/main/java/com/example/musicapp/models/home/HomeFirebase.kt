package com.example.musicapp.models.home

import java.io.Serializable

class HomeFirebase(
    val top : ArrayList<Top> = arrayListOf(),
    val todaySong : ArrayList<Top> = arrayListOf(),
    val newSong : ArrayList<Top> = arrayListOf(),
    val albums : ArrayList<Top> = arrayListOf(),
    val topSong : ArrayList<Top> = arrayListOf(),
    val top100 : ArrayList<Music> = arrayListOf(),

    ):Serializable {}
class Top(
    val image : String = "",
    val name : String = "",
    val artist : String? = null
){}