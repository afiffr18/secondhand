package com.and2t2.secondhand.data.remote

import com.and2t2.secondhand.data.remote.dto.buyer.BuyerOrderDto
import retrofit2.http.GET
import retrofit2.http.Header

interface BuyerService {

    @GET("buyer/order")
    fun getBuyerOrder(@Header("access_token") token : String) : BuyerOrderDto
}