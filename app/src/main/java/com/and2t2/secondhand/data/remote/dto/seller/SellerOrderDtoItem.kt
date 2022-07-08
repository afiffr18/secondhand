package com.and2t2.secondhand.data.remote.dto.seller

import com.google.gson.annotations.SerializedName

data class SellerOrderDtoItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("product_id")
    val productId: Int,
    @SerializedName("buyer_id")
    val buyerId: Int,
    @SerializedName("price")
    val price: Int,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("base_price")
    val basePrice: String,
    @SerializedName("image_product")
    val imageProduct: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("Product")
    val product: Product,
    @SerializedName("transaction_date")
    val transactionDate: Int,
    @SerializedName("User")
    val user: User
)