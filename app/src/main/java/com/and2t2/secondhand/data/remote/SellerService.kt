package com.and2t2.secondhand.data.remote

import com.and2t2.secondhand.data.remote.dto.seller.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface SellerService {

    /** seller banner **/


    @GET("seller/banner")
    suspend fun getSellerBanner() : SellerBannerDtoX

    //need review
    @DELETE("seller/banner/{id}")
    fun deleteSellerBanner(
        @Header("access_token") token : String,
        @Path("id") id : Int
    ) : Int


    /** seller category **/

    @GET("seller/category")
    suspend fun getSellerCategory() : SellerCategoryDto

    @GET("seller/category/{id}")
    fun getSellerCategoryId(
        @Path("id") id : Int
    ) : SellerCategoryDtoItem

    @Multipart
    @POST("seller/category")
    fun setSellerCategory(
        @Part("name") name : RequestBody
    ) : SellerCategoryDtoItem

    //need review
    @DELETE("seller/category/{id}")
    fun deleteSellerCategory(
        @Path("id") id : Int
    ) : Int

    /** Seller Product **/

    @GET("seller/product")
    suspend fun getSellerProduct(
        @Header("access_token") token :String
    ) : SellerProductDto

    @GET("seller/product/{id}")
    suspend fun getSellerProductById(
        @Header("access_token") token : String,
        @Path("id") id : Int
    ) : Response<SellerProductDtoItem>

    @Multipart
    @POST("seller/product")
    suspend fun setSellerProduct(
        @Header("access_token") token : String,
        @Part("name") name: RequestBody,
        @Part("description") description : RequestBody,
        @Part("base_price") basePrice : RequestBody,
        @Part("category_ids") categoryId : RequestBody,
        @Part("location") location : RequestBody,
        @Part image : MultipartBody.Part?
    ) : Response<SellerProductDtoPutPost>

    @Multipart
    @PUT("seller/product/{id}")
    suspend fun updateSellerProduct(
        @Header("access_token") token : String,
        @Path("id") id : Int,
        @Part("name") name: RequestBody?,
        @Part("description") description : RequestBody?,
        @Part("base_price") basePrice : RequestBody?,
        @Part("category_ids") categoryId : RequestBody?,
        @Part("location") location : RequestBody?,
        @Part image : MultipartBody.Part?
    ) : Response<SellerProductDtoPutPost>

    @DELETE("seller/product/{id}")
    suspend fun deleteSellerProduct(
        @Header("access_token") token : String,
        @Path("id") id : Int
    ) : Response<SellerProductDtoDelete>

    @PATCH("seller/product/{id}")
    suspend fun updateProductStatus(
        @Header("access_token") access_token: String,
        @Path("id") id : Int,
        @Body status: SellerOrderStatusBody
    ) : SellerProductPatchDto

    /** Seller Order **//** Status : Delayed for @PATCH(seller/order/{id}) and @GET(seller/order/product/{product_id})**/

    @GET("seller/order")
    suspend fun getSellerOrder(
        @Header("access_token") token : String,
        @Query("status") status : String?
    ) : SellerOrderDto

    // need review
    @GET("seller/order/{id}")
    suspend fun getSellerOrderById(
        @Header("access_token") token : String,
        @Path("id") id : Int
    ) : SellerOrderDtoItem

    @PATCH("seller/order/{id}")
    suspend fun updateSellerOrderStatus(
        @Header("access_token") access_token : String,
        @Path("id") id : Int,
        @Body status: SellerOrderStatusBody
    ) : SellerOrderStatusDto
}