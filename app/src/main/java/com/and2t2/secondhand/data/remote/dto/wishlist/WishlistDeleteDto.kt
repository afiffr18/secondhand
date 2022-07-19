package com.and2t2.secondhand.data.remote.dto.wishlist


import com.google.gson.annotations.SerializedName

data class WishlistDeleteDto(
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String
)