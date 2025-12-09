package com.medioambiente.network

import android.content.Context
import com.medioambiente.models.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object ApiClient {

    private const val BASE_URL = "https://adamix.net/medioambiente/"

    private var retrofit: Retrofit? = null
    private var apiService: ApiService? = null


    fun initialize(context: Context) {
        val authInterceptor = AuthInterceptor(context)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        apiService = retrofit?.create(ApiService::class.java)
    }

    val apiServiceInstance: ApiService
        get() = apiService ?: throw IllegalStateException("ApiClient no ha sido inicializado. Llama a initialize(context) en tu clase Application.")
}