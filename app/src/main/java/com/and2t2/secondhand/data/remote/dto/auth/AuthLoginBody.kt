package com.and2t2.secondhand.data.remote.dto.auth


import com.google.gson.annotations.SerializedName

data class AuthLoginBody(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)