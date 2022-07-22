package com.and2t2.secondhand.data.remote.dto.wishlist

import com.google.gson.annotations.SerializedName

data class PostWishlistBody(
    @SerializedName("product_id")
    val product_id : Int
)
