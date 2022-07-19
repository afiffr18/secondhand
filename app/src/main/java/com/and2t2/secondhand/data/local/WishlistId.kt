package com.and2t2.secondhand.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WishlistId(
    @PrimaryKey(autoGenerate = false) val productId: Int,
    )