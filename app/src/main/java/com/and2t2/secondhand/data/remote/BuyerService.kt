package com.and2t2.secondhand.data.remote

import com.and2t2.secondhand.data.remote.dto.buyer.BuyerOrderDto
import com.and2t2.secondhand.data.remote.dto.buyer.BuyerProductDtoItem
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BuyerService {

    @GET("buyer/order")
    fun getBuyerOrder(@Header("access_token") token : String) : BuyerOrderDto

    @GET("buyer/product/{id}")
    suspend fun getBuyerProductById(@Path("id") id : Int) : BuyerProductDtoItem
}