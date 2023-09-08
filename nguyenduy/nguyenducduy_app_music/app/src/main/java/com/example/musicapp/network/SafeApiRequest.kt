package com.example.musicapp.network
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun <T : Any> makeApiRequest(work: suspend () -> Response<T>): T {
        try {
            val response = work.invoke()

            if (response.isSuccessful) {
                if (response.body() == null) {

                    Log.e("API->", "response null")
                    throw Exception(
                        response.code().toString()
                    )
                }else {
                    Log.e("API->", "response returned")
                    return response.body()!!
                }
            } else {
                Log.e("API->", "response Error")
                val error = response.errorBody()?.charStream()?.readText()
                if (error != null) {
                    Log.e("API ERROR BODY = ", error)
                    try {
                        val obj = JSONObject(error)
                        val message = obj.getString("message")
                        Log.e("API ERROR JSON = ",message)
                        throw Exception(message)

                    }catch(e: JSONException){
                        Log.e("JSON ERROR ->", e.localizedMessage!!)
                        throw e
                    }
                }else{
                    throw Exception(
                        response.code().toString()
                    )
                }
            }
        }catch (e:java.lang.Exception){
            Log.e("ERROR RESPONSE",e.localizedMessage!!)
            throw e
        }
    }
}