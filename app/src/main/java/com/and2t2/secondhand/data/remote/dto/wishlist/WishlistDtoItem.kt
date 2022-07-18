package com.and2t2.secondhand.data.remote.dto.wishlist


import com.google.gson.annotations.SerializedName

data class WishlistDtoItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("Product")
    val product: Product,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("user_id")
    val userId: Int
)