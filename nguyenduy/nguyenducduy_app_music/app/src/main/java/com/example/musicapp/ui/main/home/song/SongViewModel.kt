package com.example.musicapp.ui.main.home.song

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.HomeFirebase
import com.example.musicapp.models.home.Music
import com.example.musicapp.network.FTApi
import com.example.musicapp.network.FTRepository
import com.example.musicapp.utils.Coroutines
import com.example.musicapp.utils.base.BaseViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class SongViewModel() : ViewModel() {

    private var arrayMusic: MutableLiveData<ArrayList<Music>>? = null
    var path : String = ""

    fun getData(): LiveData<ArrayList<Music>>? {
        if (arrayMusic == null) {
            arrayMusic = MutableLiveData()
            getDataSuggest()
        }
        return arrayMusic
    }
     fun getDataSuggest(){
         PlayerManager.database.getReference(path).addValueEventListener(object :
             ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 val musics: ArrayList<Music> = ArrayList()
                 for (music in snapshot.children) {
                     val item: Music? = music.getValue(Music::class.java)
                     item?.let { mu ->
                         musics.add(mu)
                     }
                 }
                 arrayMusic?.value = musics
                 Log.e("musics", "musics")
             }
             override fun onCancelled(error: DatabaseError) {
                 Log.e("database", "loadPost:onCancelled "+ error.message.toString())
             }
         })
    }
}