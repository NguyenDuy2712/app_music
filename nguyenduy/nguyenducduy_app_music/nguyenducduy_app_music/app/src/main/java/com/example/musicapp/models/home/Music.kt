package com.example.musicapp.models.home

import java.io.Serializable

class Music(
    val id: String = "",
    val nameSong: String = "",
    val artist: String? = null,
    val url: String = "",
    val image: String = "",
    val thumb: String? = null,
    val path: String? = null
    ) : Serializable {

    companion object {
    }

}