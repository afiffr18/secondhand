package com.and2t2.secondhand.data.remote

import com.and2t2.secondhand.data.remote.dto.seller.SellerBannerBody
import com.and2t2.secondhand.data.remote.dto.seller.SellerBannerDto
import com.and2t2.secondhand.data.remote.dto.seller.SellerBannerDtoItem
import retrofit2.http.*


interface SellerService {

    //seller banner

    @GET("seller/banner")
    fun getSellerBanner(@Header("access_token") token : String) : SellerBannerDto

    @POST("seller/banner")
    fun setSellerBanner(@Header("access_token") token : String,@Body sellerBannerBody: SellerBannerBody) : SellerBannerDtoItem

    @GET("seller/banner/{id}")
    fun getSellerBannerId(@Header("access_token") token : String,@Path("id") id : Int) : SellerBannerDtoItem

    @DELETE("seller/banner/{id}")
    fun deleteSellerBanner(@Header("access_token") token : String,@Path("id") id : Int) : Int

}