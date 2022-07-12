package com.and2t2.secondhand.data.remote.dto.seller


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("base_price")
    val basePrice: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("image_name")
    val imageName: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("User")
    val user: User,
    @SerializedName("user_id")
    val userId: Int
)