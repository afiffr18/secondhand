package com.and2t2.secondhand.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SellerCategory(
    @PrimaryKey val id : Int,
    @ColumnInfo(name = "name")val name : String
)