package com.and2t2.secondhand.domain.model

import com.google.gson.annotations.SerializedName

data class Notifikasi (
    val id: Int? = null,
    val bidPrice: Int? = null,
    val productId : Int? = null,
    val status: String? = null,
    val transactionDate: String? = null,
    val updatedAt: String? = null
)