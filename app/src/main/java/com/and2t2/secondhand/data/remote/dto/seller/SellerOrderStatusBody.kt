package com.and2t2.secondhand.data.remote.dto.seller


import com.google.gson.annotations.SerializedName

data class SellerOrderStatusBody(
    @SerializedName("status")
    val status: String
)