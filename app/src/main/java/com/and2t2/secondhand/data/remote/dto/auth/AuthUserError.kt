package com.and2t2.secondhand.data.remote.dto.auth


import com.google.gson.annotations.SerializedName

data class AuthUserError(
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String
)