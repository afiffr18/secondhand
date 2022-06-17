package com.and2t2.secondhand.data.remote.dto.buyer


import com.google.gson.annotations.SerializedName

data class BuyerOrderDtoItem(
    @SerializedName("buyer_id")
    val buyerId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("price")
    val price: Int,
    @SerializedName("Product")
    val product: Product,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("User")
    val user: User
)