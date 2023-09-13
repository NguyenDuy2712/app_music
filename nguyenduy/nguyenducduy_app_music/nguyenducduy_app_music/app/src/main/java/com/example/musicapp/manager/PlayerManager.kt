package com.example.musicapp.manager

import android.app.Activity
import android.content.*
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.MusicApplication
import com.example.musicapp.models.home.Music
import com.example.musicapp.network.FTApi
import com.example.musicapp.network.FTRepository
import com.example.musicapp.services.PlayerService
import com.example.musicapp.utils.Coroutines
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


object PlayerManager {
    private val mConnectionMap = WeakHashMap<Context, ServiceBinder>()

    var playerService : PlayerService? = PlayerService()
     lateinit var database: FirebaseDatabase

    var music : MutableLiveData<ArrayList<Music>>? = MutableLiveData()

    var musicLocal :ArrayList<Music> = arrayListOf()
    var changeSong : MutableLiveData<Boolean> = MutableLiveData(false)
    var isPlaying : MutableLiveData<Boolean> = MutableLiveData(false)
    var api : String = "top100"


    fun getListMusic(): ArrayList<Music>? {
        return music?.value
    }
    fun playPause() {
        if (playerService != null)
            playerService?.playOrPause()
    }
     fun queryAudio(){

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME
        )

        MusicApplication.appContext.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val titleColIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistColIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val pathIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val displayName = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)

            Log.e("TAG", "Query found ${cursor.count} rows")

            while (cursor.moveToNext()) {
                val id = cursor.getInt(idIndex)
                val title = cursor.getString(titleColIndex)
                val artist = cursor.getString(artistColIndex)
                val path = cursor.getString(pathIndex)
                val name = cursor.getString(displayName)
                val artistLocal = if(name.contains("-")) name.split("-")[1] else artist
                val songLocal = if(name.contains("-")) name.split("-")[0] else title
                Log.e("TAG", "artistLocal found ${artistLocal}")
                Log.e("TAG", "songLocal found ${songLocal}")
                Log.e("TAG", "artist found ${artist}")




                val ext = name.substring(name.lastIndexOf(".") + 1)
                if (ext == "mp3") {
                    //here you can add your mp3 file to songList
                    val mus = Music(id.toString(),songLocal,artistLocal,"","","",path)
                    musicLocal.add(mus)
                    if(AppPreference.playStatus == 1){
                        music?.value = musicLocal
                    }
                }

            }
        }
    }
    fun getMusic(callback:(Boolean) -> Unit){
        Log.e("saveAPI : ",AppPreference.saveAPI)
        Log.e("indexPlay : ","index "+AppPreference.indexPlaying)
        PlayerManager.database.getReference(AppPreference.saveAPI).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val musics: ArrayList<Music> = ArrayList()
                for (music in snapshot.children) {
                    val item: Music? = music.getValue(Music::class.java)
                    item?.let { mu ->
                        musics.add(mu)
                    }
                }
                music?.value = musics
                callback(true)
                Log.e("musics", "musics")
            }
            override fun onCancelled(error: DatabaseError) {
                callback(false)
                Log.e("database", "loadPost:onCancelled "+ error.message.toString())
            }
        })
    }
    fun bindToService(context: Context, callback: ServiceConnection): ServiceToken? {

        var realActivity: Activity? = (context as Activity).parent
        if (realActivity == null) {
            realActivity = context
        }

        val contextWrapper = ContextWrapper(realActivity)
        val intent = Intent(contextWrapper, PlayerService::class.java)
        try {
            contextWrapper.startService(intent)
        } catch (ignored: IllegalStateException) {
            ContextCompat.startForegroundService(context, intent)
        }
        val binder = ServiceBinder(callback)

        if (contextWrapper.bindService(
                Intent().setClass(contextWrapper, PlayerService::class.java),
                binder,
                Context.BIND_AUTO_CREATE
            )
        ) {
            mConnectionMap[contextWrapper] = binder
            return ServiceToken(contextWrapper)
        }
        return null
    }

    class ServiceBinder internal constructor(private val mCallback: ServiceConnection?) :
        ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as PlayerService.MyBinder
            playerService = binder.currentService()
            mCallback?.onServiceConnected(className, service)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            mCallback?.onServiceDisconnected(className)
            playerService = null
        }
    }

    class ServiceToken internal constructor(internal var mWrappedContext: ContextWrapper)

}
