package com.and2t2.secondhand.data.remote

import com.and2t2.secondhand.data.remote.dto.buyer.*
import retrofit2.Response
import retrofit2.http.*

interface BuyerService {

    @GET("buyer/order")
    fun getBuyerOrder(@Header("access_token") token : String) : BuyerOrderDto

    @POST("buyer/order")
    suspend fun postBuyerOrder(
        @Body postBuyerOrderBody: PostBuyerOrderBody,
        @Header("access_token") token : String) : Response<PostBuyerOrderDto>


    @GET("buyer/product/{id}")
    suspend fun getBuyerProductById(@Path("id") id : Int) : BuyerProductDtoItem

    @GET("buyer/product")
    suspend fun getBuyerProduct(
        @Query("status") status : String?,
        @Query("category_id") categoryId : Int?,
        @Query("search") search : String?
    ) : BuyerProductDto


}