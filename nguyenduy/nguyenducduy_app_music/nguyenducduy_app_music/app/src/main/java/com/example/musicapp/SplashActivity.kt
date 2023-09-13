package com.example.musicapp

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.musicapp.database.MusicDB
import com.example.musicapp.databinding.LoginActivityBinding
import com.example.musicapp.databinding.SplashActivityBinding
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.models.home.Music
import com.example.musicapp.ui.LoginActivity
import com.example.musicapp.utils.UIApplicationUtils
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : SplashActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UIApplicationUtils.transparentStatusBar(this, true)
        setUpView()

    }
    private fun setUpView() {
        PlayerManager.database = Firebase.database
//        if(!AppPreference.isLogin){
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finishAffinity()
//        }else{
//            getSong()
//        }
        getSong()
    }
    fun gotoMainActivity(delayMili : Long){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }, delayMili)
    }

    private fun getSong(){
        if(AppPreference.playStatus == 1){
            gotoMainActivity(1500)
        }else if (AppPreference.playStatus == 0){
            if(AppPreference.saveAPI != "null"){
                PlayerManager.getMusic(){
                    gotoMainActivity(1000)
                }
            }else{
                gotoMainActivity(1500)
            }
        }else{
            val arrayMusic = ArrayList<Music>()
            MusicDB.getDB().forEach {
                val music = Music(it.id,it.nameSong,it.artist,it.url,it.image,it.thumb,it.path)
                arrayMusic.add(music)
            }
            PlayerManager.music?.value = arrayMusic
            gotoMainActivity(1000)
        }
    }



}