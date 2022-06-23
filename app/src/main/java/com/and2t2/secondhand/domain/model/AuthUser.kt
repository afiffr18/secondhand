package com.and2t2.secondhand.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AuthUser(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "fullName") val fullName: String? = null,
    @ColumnInfo(name = "city") val city : String? = null,
    @ColumnInfo(name = "address") val address: String? = null,
    @ColumnInfo(name = "phoneNumber") val phoneNumber: String? = null,
    @ColumnInfo(name = "imageUrl") val imageUrl: String? = null
)
