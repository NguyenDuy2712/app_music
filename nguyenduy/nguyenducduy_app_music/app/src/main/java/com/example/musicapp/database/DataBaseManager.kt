package com.example.musicapp.database

import io.realm.Realm
import io.realm.RealmConfiguration

object DataBaseManager {
    lateinit var realm : Realm
    fun setUpRealm(){
        val realmName: String = "MusicApp"
        val config = RealmConfiguration.Builder().name(realmName)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
        realm = Realm.getInstance(config)
    }
}