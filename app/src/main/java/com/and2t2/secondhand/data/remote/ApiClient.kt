package com.and2t2.secondhand.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://market-final-project.herokuapp.com/"

    /**
     * Bikin interceptor
     */
    private val logging: HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }
        }

    //create object client untuk retorfit
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    //Membuat instance

    val instanceNotifikasi: NotifikasiService by lazy {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit.create(NotifikasiService::class.java)
    }

    val instanceSeller: SellerService by lazy {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit.create(SellerService::class.java)
    }

    val instanceBuyer: BuyerService by lazy {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit.create(BuyerService::class.java)
    }

    val INSTANCE_AUTH : AuthService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        retrofit.create(AuthService::class.java)
    }
}