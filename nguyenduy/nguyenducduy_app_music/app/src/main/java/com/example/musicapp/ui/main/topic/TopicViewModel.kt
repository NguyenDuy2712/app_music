package com.example.musicapp.ui.main.topic

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.Music
import com.example.musicapp.models.home.Topic
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TopicViewModel : ViewModel() {
    private var arrayTopic: MutableLiveData<ArrayList<Topic>>? = null
    var path : String = ""

    fun getData(): LiveData<ArrayList<Topic>>? {
        if (arrayTopic == null) {
            arrayTopic = MutableLiveData()
            getDataSuggest()
        }
        return arrayTopic
    }
    fun getDataSuggest(){
        PlayerManager.database.getReference("topic").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val musics: ArrayList<Topic> = ArrayList()
                for (music in snapshot.children) {
                    val item: Topic? = music.getValue(Topic::class.java)
                    item?.let { mu ->
                        musics.add(mu)
                    }
                }
                arrayTopic?.value = musics
                Log.e("musics", "musics")
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("database", "loadPost:onCancelled "+ error.message.toString())
            }
        })
    }
}