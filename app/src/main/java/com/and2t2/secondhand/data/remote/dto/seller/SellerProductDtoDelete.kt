package com.and2t2.secondhand.data.remote.dto.seller


import com.google.gson.annotations.SerializedName

data class SellerProductDtoDelete(
    @SerializedName("msg")
    val message: String,
    @SerializedName("name")
    val name: String
)