package com.and2t2.secondhand.data.remote.dto.auth


import com.google.gson.annotations.SerializedName

data class AuthLoginDtoItem(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)