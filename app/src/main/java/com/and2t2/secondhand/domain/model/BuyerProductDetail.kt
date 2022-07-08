package com.and2t2.secondhand.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.and2t2.secondhand.data.remote.dto.buyer.Category

@Entity
data class BuyerProductDetail(
    @PrimaryKey val id : Int,
    @ColumnInfo(name = "namaBarang") val namaBarang : String?=null,
    @ColumnInfo(name = "deskripsiBarang")val deskripsiBarang : String?=null,
    @ColumnInfo(name = "hargaBarang")val hargaBarang : Int,
    @ColumnInfo(name = "imageUrl")val imageUrl : String? =null,
    @ColumnInfo(name = "lokasi")val lokasi : String?=null,
    @ColumnInfo(name = "kategori")val kategori : String,
    @ColumnInfo(name = "userImage")val imageUser : String?=null,
    @ColumnInfo(name = "username")val username : String
)