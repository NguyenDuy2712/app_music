package com.example.musicapp.ui.main.leaderboard.detailLeader

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.Artist
import com.example.musicapp.models.home.Music
import com.example.musicapp.network.FTApi
import com.example.musicapp.network.FTRepository
import com.example.musicapp.utils.Coroutines
import com.example.musicapp.utils.base.BaseViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DetailLeaderViewModel(private val repository: FTRepository = FTRepository(FTApi())) : BaseViewModel() {

    private var arrayMusic: MutableLiveData<ArrayList<Music>>? = null
    private var arrayArtist: MutableLiveData<ArrayList<Artist>>? = null


    fun getMusic(): LiveData<ArrayList<Music>>? {
        if (arrayMusic == null) {
            arrayMusic = MutableLiveData()
            getData()
        }
        return arrayMusic
    }
    fun getArtist(): LiveData<ArrayList<Artist>>? {
        if (arrayArtist == null) {
            arrayArtist = MutableLiveData()
            getDataArtist()
        }
        return arrayArtist
    }
    fun getData(){
        PlayerManager.database.getReference(PlayerManager.api).addValueEventListener(object :
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
    fun getDataArtist(){
        PlayerManager.database.getReference("artist").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val artists: ArrayList<Artist> = ArrayList()
                for (music in snapshot.children) {
                    val item: Artist? = music.getValue(Artist::class.java)
                    item?.let { mu ->
                        artists.add(mu)
                    }
                }
                arrayArtist?.value = artists
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("database", "loadPost:onCancelled "+ error.message.toString())
            }
        })
    }
}