package com.example.musicapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.musicapp.MusicApplication
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.services.PlayerService


class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
       when(intent.action){
           MusicApplication.NEXT ->{
               startService(context, MusicApplication.NEXT)
               PlayerManager.playerService?.nextSong()
           }
           MusicApplication.PREVIOUS ->{
               startService(context, MusicApplication.PREVIOUS)
               PlayerManager.playerService?.previousSong()
           }
           MusicApplication.PLAY ->{
               startService(context, MusicApplication.PLAY)
               PlayerManager.playerService?.playOrPause()
               PlayerManager.playerService?.showNotification()
           }
           MusicApplication.EXIT ->{
               PlayerManager.playerService?.cancelNotifi()
           }
       }
    }
    private fun startService(context: Context, command: String?) {
        if (PlayerManager.playerService == null) {
            val intent = Intent(context, PlayerService::class.java)
            intent.action = command
            try {
                // IMPORTANT NOTE: (kind of a hack)
                // on Android O and above the following crashes when the app is not running
                // there is no good way to check whether the app is running so we catch the exception
                // we do not always want to use startForegroundService() because then one gets an ANR
                // if no notification is displayed via startForeground()
                // according to Play analytics this happens a lot, I suppose for example if command = PAUSE
                context.startService(intent)
            } catch (ignored: IllegalStateException) {
                ContextCompat.startForegroundService(context, intent)
            }
        }
    }
}