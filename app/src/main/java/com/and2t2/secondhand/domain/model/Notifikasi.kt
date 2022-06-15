package com.and2t2.secondhand.domain.model

import com.google.gson.annotations.SerializedName

data class Notifikasi (
    val id: Int,
    val bidPrice: Int,
    val status: String,
    val transactionDate: String,
    val updatedAt: String
)