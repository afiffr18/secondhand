package com.and2t2.secondhand.data.remote.dto.buyer


import com.google.gson.annotations.SerializedName

data class BuyerWishlistDtoItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("Product")
    val product: ProductXX,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("user_id")
    val userId: Int
)