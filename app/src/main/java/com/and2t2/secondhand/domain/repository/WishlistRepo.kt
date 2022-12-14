package com.and2t2.secondhand.domain.repository

import com.and2t2.secondhand.data.remote.BuyerService
import com.and2t2.secondhand.data.remote.dto.wishlist.PostWishlistBody
import com.and2t2.secondhand.data.remote.dto.wishlist.PostWishlistDto
import com.and2t2.secondhand.data.remote.dto.wishlist.WishlistDeleteDto
import com.and2t2.secondhand.data.remote.dto.wishlist.WishlistDto
import com.and2t2.secondhand.domain.model.Wishlist
import com.and2t2.secondhand.domain.model.WishlistMapper
import retrofit2.Response

class WishlistRepo(
    val apiService : BuyerService,
    val mapper : WishlistMapper
) {

    suspend fun getBuyerWishlist(access_token : String) : List<Wishlist>{
        val result = apiService.getBuyerWishlist(access_token)
        return mapper.toDomainList(result)
    }

    suspend fun postBuyerWishlist(access_token: String,postWishlistBody: PostWishlistBody) : Response<PostWishlistDto>{
       return apiService.getPostBuyerWishlist(access_token, postWishlistBody)
    }


    suspend fun deleteBuyerWishlist(access_token: String,id : Int) : Response<WishlistDeleteDto>{
        return apiService.deleteWishlist(access_token, id)
    }
}