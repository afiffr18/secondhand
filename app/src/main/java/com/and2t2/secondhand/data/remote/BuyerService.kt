package com.and2t2.secondhand.data.remote

import com.and2t2.secondhand.data.remote.dto.buyer.*
import retrofit2.Response
import retrofit2.http.*

interface BuyerService {

    @GET("buyer/order")
    fun getBuyerOrder(@Header("access_token") token : String) : BuyerOrderDto

    @GET("buyer/product/{id}")
    suspend fun getBuyerProductById(@Path("id") id : Int) : BuyerProductDtoItem

    @POST("buyer/order")
    suspend fun postBuyerOrder(
        @Body postBuyerOrderBody: PostBuyerOrderBody,
        @Header("access_token") token : String) : Response<PostBuyerOrderDto>


}