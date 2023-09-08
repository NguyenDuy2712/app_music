package com.example.musicapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES
import com.example.musicapp.database.DataBaseManager
import com.example.musicapp.manager.AppPreference
import io.realm.Realm
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class MusicApplication:Application() {

    companion object {
        lateinit var appInstance: MusicApplication
            private set
        val appContext: Context by lazy {
            appInstance.applicationContext
        }
        const val CHANNEL_ID = "channel"
        const val PLAY = "play"
        const val NEXT = "next"
        const val PREVIOUS = "previous"
        const val EXIT = "exit"
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        AppPreference.init(this)
        Realm.init(this)
        DataBaseManager.setUpRealm()
        if(Build.VERSION.SDK_INT >= VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID,"now playing song",NotificationManager.IMPORTANCE_LOW)
            notificationChannel.description = "this is a important song"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationChannel.setSound(null,null)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}