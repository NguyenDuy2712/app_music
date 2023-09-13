package com.example.musicapp.services

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.transition.Transition
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.musicapp.MainActivity
import com.example.musicapp.MusicApplication
import com.example.musicapp.R
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.receivers.NotificationReceiver
import com.example.musicapp.utils.getSongThumbnail
import com.example.musicapp.utils.loadImagePicasso
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class PlayerService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
    MediaPlayer.OnErrorListener {

    private lateinit var mediaSession: MediaSessionCompat
    var mediaPlayer: MediaPlayer? = null
    private var myIBinder = MyBinder()

//    val receiverPlay: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            playIndex()
//        }
//    }

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(this, "my music")
//        val intenFilter = IntentFilter("play_music")
//        registerReceiver(receiverPlay, intenFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) mediaPlayer?.release()
        mediaPlayer = null
    //    unregisterReceiver(receiverPlay)
        stopForeground(true)
    }

    fun stopAudio() {
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
    }
    fun pause() {
        mediaPlayer?.pause()
    }

    fun start() {
        if(mediaPlayer != null){
            mediaPlayer?.start()
        }else{
            previousSong()
           // playIndex()
        }
    }

    fun nextSong() {
        var music = PlayerManager.music?.value

        if (AppPreference.indexPlaying < music?.size?.minus(1)!!) {
            AppPreference.indexPlaying += 1
        } else {
            AppPreference.indexPlaying = 0
        }
        playIndex()
    }

    fun previousSong() {
        var music = PlayerManager.music?.value

        if (AppPreference.indexPlaying > 0) {
            AppPreference.indexPlaying -= 1
        } else {
            AppPreference.indexPlaying = music?.size?.minus(1)!!
        }
        playIndex()
    }

    fun playOrPause() {
        PlayerManager.isPlaying.value = !PlayerManager.isPlaying.value!!
        Log.e("isPlaying : ",PlayerManager.isPlaying.value.toString())
        if (PlayerManager.isPlaying.value!!) {
            start()
        } else {
            pause()
        }
        showNotification()
    }

    fun getCurrentPosition(): Int {
        mediaPlayer?.let {
            return it.currentPosition
        }
        return 0
    }

    fun getDurationPosition(): Int {
        if (mediaPlayer != null) {
            return mediaPlayer!!.duration
        }
        return -1
    }

    fun seekTo(progress: Int) {
        mediaPlayer?.seekTo(progress)
    }

    fun isPlaying(): Boolean {
        return PlayerManager.isPlaying.value!!
    }

    override fun onBind(intent: Intent): IBinder {
        return myIBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): PlayerService {
            return this@PlayerService
        }
    }
    fun cancelNotifi(){
        if(mediaPlayer != null){
            pause()
            mediaPlayer?.release()
            mediaPlayer = null
        }
        stopForeground(true)
        PlayerManager.isPlaying.value = false
    }

    @SuppressLint("RemoteViewLayout")
    fun showNotification() {
        var music = PlayerManager.music?.value

        var song = music?.get(AppPreference.indexPlaying)

        val context = MusicApplication.appContext

        val nextIntent =
            Intent(context, NotificationReceiver::class.java).setAction(MusicApplication.NEXT)
        val nextPendingIntent =
            PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val previousIntent =
            Intent(context, NotificationReceiver::class.java).setAction(MusicApplication.PREVIOUS)
        val previousPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            previousIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val playIntent =
            Intent(context, NotificationReceiver::class.java).setAction(MusicApplication.PLAY)
        val playPendingIntent =
            PendingIntent.getBroadcast(context, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent =
            Intent(context, NotificationReceiver::class.java).setAction(MusicApplication.EXIT)
        val exitPendingIntent =
            PendingIntent.getBroadcast(context, 0, exitIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val mainIntent = Intent(context, MainActivity::class.java)
        val mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent,
            PendingIntent.FLAG_IMMUTABLE)

        val notificationLayout = RemoteViews(packageName, R.layout.notification)
        notificationLayout.setOnClickPendingIntent(
            R.id.btnNotiNext,
            nextPendingIntent
        );
        notificationLayout.setOnClickPendingIntent(
            R.id.btnNotiCancel,
            exitPendingIntent
        );
        notificationLayout.setOnClickPendingIntent(
            R.id.btnNotiPrevious,
            previousPendingIntent
        );
        notificationLayout.setOnClickPendingIntent(
            R.id.btnNotiPlay,
            playPendingIntent
        );
        notificationLayout.setTextViewText(
            R.id.lbTitleNoti,
            music?.get(AppPreference.indexPlaying)!!.nameSong
        )
        notificationLayout.setTextViewText(
            R.id.lbSubTitleNoti,
            music?.get(AppPreference.indexPlaying)!!.artist
        )
        var playPauseDrawable = R.drawable.ic_pause
        if (mediaPlayer != null)
            playPauseDrawable = if (PlayerManager.isPlaying.value == true) {
                R.drawable.ic_pause
            } else {
                R.drawable.ic_play
            }
        notificationLayout.setImageViewResource(
            R.id.btnNotiPlay,
            playPauseDrawable)

        Log.e("song : ", song?.nameSong.toString())

        val notification =
            NotificationCompat.Builder(baseContext, MusicApplication.CHANNEL_ID).setOngoing(true)
                .apply {
                    setContentIntent(mainPendingIntent)
                    setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    priority = NotificationCompat.PRIORITY_LOW
                    setSmallIcon(R.drawable.ic_song)
                    setSilent(true)
                    setCustomContentView(notificationLayout)
                }

        startForeground(5, notification.build())

    }

    fun playIndex() {
        var arrayMusic = PlayerManager.music?.value
        arrayMusic?.let {
            val url =  it[AppPreference.indexPlaying].url
            val path =  it[AppPreference.indexPlaying].path
            if(path.isNullOrEmpty()){
                playAudio(url)
            }else{
                playLocal(path)
            }
        }
    }

    fun playAudio(audioUrl: String) {
        if(mediaPlayer != null){
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
        PlayerManager.changeSong.value = true

        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.setOnCompletionListener(this)
        mediaPlayer?.apply {
//            setAudioAttributes(
//                AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .build()
//            )
            try {
                mediaPlayer?.setDataSource(audioUrl)
                mediaPlayer?.prepareAsync()
            }
            catch (e:ExceptionInInitializerError){

            }
        }
    }
    fun playLocal(audioUrl: String) {

        if(mediaPlayer != null){
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
        PlayerManager.changeSong.value = true

        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.setOnCompletionListener(this)
        mediaPlayer?.apply {
            try {
                mediaPlayer?.setDataSource(audioUrl)
                mediaPlayer?.prepare()
                play()
            }
            catch (e:ExceptionInInitializerError){

            }
        }
    }

    fun play() {
        mediaPlayer?.let {
            PlayerManager.isPlaying.value = true
            it.start()
        }
        showNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }
    inner class LocalBinder : Binder() {
        fun getService(): PlayerService {
            return this@PlayerService
        }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        play()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if(AppPreference.repeatSong){
            play()
        }else{
            nextSong()
        }
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mediaPlayer?.stop()
        Toast.makeText(this, "Invalid format or song", Toast.LENGTH_SHORT).show()
        stopForeground(false)
        return false

    }

}