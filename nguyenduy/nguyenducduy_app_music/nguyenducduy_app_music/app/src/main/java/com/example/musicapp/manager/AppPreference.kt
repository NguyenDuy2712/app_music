package com.example.musicapp.manager

import android.content.Context
import android.content.SharedPreferences

object AppPreference {
    private const val NAME = "FTPreferences"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    private val INDEX_PLAYING = Pair("index_playing", 0)
    private val API_PlAYING = Pair("api_playing", "top100")
    private val PLAY_STATUS = Pair("play_status",0)
    private val REPEAT_SONG = Pair("repeat_song",false)
    private val USER_NAME = Pair("user_name","Bui Duy")
    private val USER_EMAIL = Pair("user_email","Email")
    private val IsLogin = Pair("isLogin",false)




    // play_status : 0-url,1 - local ,2-playlist


    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }
    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }
    var indexPlaying : Int
        get() = preferences.getInt(INDEX_PLAYING.first,INDEX_PLAYING.second)
        set(value) = preferences.edit {
            it.putInt(INDEX_PLAYING.first,value)
        }
    var saveAPI : String
        get() = preferences.getString(API_PlAYING.first,API_PlAYING.second)!!
        set(value) = preferences.edit {
            it.putString(API_PlAYING.first,value)
        }
    var playStatus : Int
        get() = preferences.getInt(PLAY_STATUS.first,PLAY_STATUS.second)
        set(value) = preferences.edit {
            it.putInt(PLAY_STATUS.first,value)
        }
    var repeatSong : Boolean
        get() = preferences.getBoolean(REPEAT_SONG.first,REPEAT_SONG.second)
        set(value) = preferences.edit {
            it.putBoolean(REPEAT_SONG.first,value)
        }
    var user_name : String?
        get() = preferences.getString(USER_NAME.first,USER_NAME.second)
        set(value) = preferences.edit {
            it.putString(USER_NAME.first,value)
        }
    var user_email : String?
        get() = preferences.getString(USER_EMAIL.first,USER_EMAIL.second)
        set(value) = preferences.edit {
            it.putString(USER_EMAIL.first,value)
        }
    var isLogin : Boolean
        get() = preferences.getBoolean(IsLogin.first,IsLogin.second)
        set(value) = preferences.edit {
            it.putBoolean(IsLogin.first,value)
        }


}