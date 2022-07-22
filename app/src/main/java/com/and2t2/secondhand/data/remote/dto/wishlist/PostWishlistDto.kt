package com.and2t2.secondhand.data.remote.dto.wishlist


import com.google.gson.annotations.SerializedName

data class PostWishlistDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("Product")
    val product: Product
)