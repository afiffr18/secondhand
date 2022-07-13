package com.and2t2.secondhand.data.remote.dto.buyer


import com.google.gson.annotations.SerializedName

data class PostBuyerWishlistDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("Product")
    val product: ProductX
)