package com.example.musicapp.network

import com.example.musicapp.BuildConfig
import com.example.musicapp.config.Config
import com.example.musicapp.models.home.Artist
import com.example.musicapp.models.home.Music
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface FTApi {
    companion object {

        operator fun invoke(): FTApi {

            val builder = OkHttpClient.Builder()
            if (BuildConfig.DEBUG){
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                builder.addInterceptor(logging)
            }

            val client = builder.build()
            return Retrofit.Builder()
                .client(client)
                .baseUrl(Config.endPointApi())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FTApi::class.java)
        }
    }

    // save api
    @GET("{api}")
    suspend fun saveAPI(@Path("api") api : String): Response<ArrayList<Music>>

    // save api
    @GET("{api}")
    suspend fun getMusic(@Path("api") api : String): Response<ArrayList<Music>>

    // get favorite
    @GET("favoriteSong")
    suspend fun getFavorite(): Response<ArrayList<Music>>
    // get top 100
    @GET("top100")
    suspend fun getTop100(): Response<ArrayList<Music>>

    @GET("Artist")
    suspend fun getArtist(): Response<ArrayList<Artist>>
}