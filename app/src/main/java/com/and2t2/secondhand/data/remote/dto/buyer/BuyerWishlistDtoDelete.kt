package com.and2t2.secondhand.data.remote.dto.buyer


import com.google.gson.annotations.SerializedName

data class BuyerWishlistDtoDelete(
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String
)