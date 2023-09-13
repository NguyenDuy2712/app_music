package com.example.musicapp.database

import com.example.musicapp.models.home.Music
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.where
import org.bson.types.ObjectId

open class MusicDB(
    @PrimaryKey
    var id: String = ObjectId().toHexString(),
    var nameSong: String = "",
    var artist: String? = null,
    var url: String = "",
    var image: String = "",
    var thumb: String? = null,
    var path: String? = null

):RealmObject(){
    companion object {
        fun saveDB(music : Music){
            DataBaseManager.realm.executeTransaction {
                val obj = MusicDB()
                obj.nameSong = music.nameSong
                obj.artist = music.artist
                obj.url = music.url
                obj.image = music.image
                obj.thumb = music.thumb
                obj.path = music.path
                it.insert(obj)
            }
        }
        fun getDB() : RealmResults<MusicDB> {
            return DataBaseManager.realm.where<MusicDB>().findAll()
        }
        fun removeAll(){
            DataBaseManager.realm.executeTransaction { realm ->
                val result = realm.where<MusicDB>().findAll()
                result.forEach {
                    it.deleteFromRealm()
                }
            }
        }
        fun delete(id:String){
            DataBaseManager.realm.executeTransaction {
                DataBaseManager.realm.where<MusicDB>().equalTo("id",id).findFirst()?.deleteFromRealm()
            }
        }
    }
}