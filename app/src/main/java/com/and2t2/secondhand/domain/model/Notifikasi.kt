package com.and2t2.secondhand.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Notifikasi (
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = "bidPrice") val bidPrice: Int? = null,
    @ColumnInfo(name = "productId") val productId : Int? = null,
    @ColumnInfo(name = "status") val status: String? = null,
    @ColumnInfo(name = "transactionDate") val transactionDate: String? = null,
    @ColumnInfo(name = "updatedAt")val updatedAt: String? = null
)