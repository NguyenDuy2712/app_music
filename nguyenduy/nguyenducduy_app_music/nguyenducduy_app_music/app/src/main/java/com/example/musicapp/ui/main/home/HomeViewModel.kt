package com.example.musicapp.ui.main.home

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.HomeFirebase
import com.example.musicapp.models.home.Music
import com.example.musicapp.utils.Coroutines
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class HomeViewModel():ViewModel() {
    private var home: MutableLiveData<HomeFirebase>? = null
    var path : String = ""

    fun getSuggest(): LiveData<HomeFirebase>? {
        if (home == null) {
            home = MutableLiveData()
            getData()
        }
        return home
    }
    fun getData(){
        PlayerManager.database.getReference(path).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                 home?.value = snapshot.getValue<HomeFirebase>()
                Log.e("home", "home")

            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("database", "loadPost:onCancelled "+ error.message.toString())
            }
        })
    }
}