package com.example.musicapp.network

import android.util.Log
import com.example.musicapp.manager.AppPreference
import com.example.musicapp.manager.PlayerManager


class FTRepository(private val api: FTApi) : SafeApiRequest() {
    suspend fun getFavorite() = makeApiRequest {
        api.getFavorite()
    }
    suspend fun getTop100()=makeApiRequest {
        api.getTop100()
    }
    suspend fun getAPISaved()=makeApiRequest {
        api.saveAPI(AppPreference.saveAPI)
    }
    suspend fun getMusic()=makeApiRequest {
        api.getMusic(PlayerManager.api)
    }
    suspend fun getArtist()=makeApiRequest {
        api.getArtist()
    }

}