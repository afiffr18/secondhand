package com.and2t2.secondhand.domain.repository

import com.and2t2.secondhand.data.remote.BuyerService
import com.and2t2.secondhand.data.remote.dto.wishlist.PostWishlistBody
import com.and2t2.secondhand.data.remote.dto.wishlist.PostWishlistDto
import com.and2t2.secondhand.domain.model.Wishlist
import com.and2t2.secondhand.domain.model.WishlistMapper

class WishlistRepo(
    private val apiService : BuyerService,
    private val mapper : WishlistMapper
) {

    suspend fun getBuyerWishlist(access_token : String) : List<Wishlist>{
        val result = apiService.getBuyerWishlist(access_token)
        return mapper.toDomainList(result)
    }

    suspend fun postBuyerWishlist(access_token: String,postWishlistBody: PostWishlistBody) = apiService.getPostBuyerWishlist(access_token, postWishlistBody)
}