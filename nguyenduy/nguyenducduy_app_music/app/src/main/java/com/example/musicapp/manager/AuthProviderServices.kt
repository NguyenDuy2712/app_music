package com.example.musicapp.manager

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.R
import com.example.musicapp.utils.getString
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


interface SocialLoginInterface {
    fun loginSuccessGoogle(account: GoogleSignInAccount)
    fun loginFailGoogle(message: String? = null)
    fun loginSuccess()

}

object AuthProviderServices {
    lateinit var socialLogin: SocialLoginInterface
    lateinit var googleClient: GoogleSignInClient



    fun configGoogleSignin(activity: AppCompatActivity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id)) //.default_web_client_id
            .requestEmail()
            .build()
        googleClient = GoogleSignIn.getClient(activity, gso)
    }

    fun onActivityGG( resultCode: Int, data: Intent?, activity: AppCompatActivity) {
        Log.e("TAG", "requestCode RESULT_OK :" + Activity.RESULT_OK)
        if (resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.result.idToken
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.e("TAG", " :" + account.email)
                Log.e("TAG", "avatar:" + account.photoUrl)
                AppPreference.user_email = account.email
                AppPreference.user_name = account.photoUrl.toString()
                socialLogin.loginSuccessGoogle(account)
            } catch (e: ApiException) {
                socialLogin.loginFailGoogle(e.toString())
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        } else {
            socialLogin.loginFailGoogle()
            AppPreference.user_email = "dduy2712@gmail.com"
            AppPreference.user_name = "https://lh3.googleusercontent.com/ogw/AGvuzYbfmYQZotQr9fIivTrOuJYe4XmrWLo-5zdOlKeu=s32-c-mo"
            socialLogin.loginSuccess()
            Log.d("TAG", "google dismiss dialog ")
        }
    }

    fun signOutGoogle(callBack:()->Unit) {
        if (::googleClient.isInitialized) {
            Log.d("TAG", "googleClient")
            googleClient.signOut().addOnCompleteListener {
                AppPreference.isLogin = false
                callBack
            }
        }
    }
}