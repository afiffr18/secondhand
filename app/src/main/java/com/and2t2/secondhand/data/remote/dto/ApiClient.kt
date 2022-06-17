package com.and2t2.secondhand.data.remote.dto

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // Base URL
    private const val BASE_URL = "https://market-final-project.herokuapp.com/"

    // Interceptor di level body
    private val logging : HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }
        }

    // Crate client untuk retrofit
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // create instance ApiService
    val instanceLogin : LoginService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        retrofit.create(LoginService::class.java)
    }
}