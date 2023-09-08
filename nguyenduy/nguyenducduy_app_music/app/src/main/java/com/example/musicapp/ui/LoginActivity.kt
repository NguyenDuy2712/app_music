package com.example.musicapp.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.musicapp.MainActivity
import com.example.musicapp.database.MusicDB
import com.example.musicapp.databinding.LoginActivityBinding
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.AuthProviderServices
import com.example.musicapp.manager.PlayerManager
import com.example.musicapp.manager.SocialLoginInterface
import com.example.musicapp.models.home.Music
import com.example.musicapp.utils.UIApplicationUtils
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class LoginActivity : AppCompatActivity(), SocialLoginInterface {
    private lateinit var binding : LoginActivityBinding

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        Log.e("registerForActivityResult","registerForActivityResult ====> $it")
        AuthProviderServices.onActivityGG(
            it.resultCode,
            it.data,
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
    }
    fun setUpView(){
        AuthProviderServices.configGoogleSignin(this)
        binding.login.setOnClickListener {
            signInGoogle()
        }
    }
    fun bind(){}
    override fun loginSuccessGoogle(account: GoogleSignInAccount) {
        AppPreference.isLogin = true
        getSong()
    }

    override fun loginFailGoogle(message: String?) {
    }

    override fun loginSuccess() {
        AppPreference.isLogin = true
        getSong()
    }

    override fun onResume() {
        super.onResume()
        AuthProviderServices.socialLogin = this
    }
    fun signInGoogle() {
        binding.loadingView.visibility = View.VISIBLE
        val signInIntent = AuthProviderServices.googleClient.signInIntent
        resultLauncher.launch(signInIntent)
    }
    fun gotoMainActivity(delayMili : Long){
        binding.loadingView.visibility = View.INVISIBLE
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}