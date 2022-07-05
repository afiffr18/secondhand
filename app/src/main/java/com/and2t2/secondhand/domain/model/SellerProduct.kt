package com.and2t2.secondhand.domain.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
data class SellerProduct(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "user_id") val userId: Int? = null,
    @ColumnInfo(name = "productName") val productName: String? = null,
    @ColumnInfo(name = "description") val description : String? = null,
    @ColumnInfo(name = "base_price") val basePrice: Int? = null,
    @ColumnInfo(name = "imageUrl") val imageUrl: String? = null,
    @ColumnInfo(name = "location") val location: String? = null,
    @ColumnInfo(name = "status") val status: String? = null,
    @ColumnInfo(name = "categories") val categories: String? = null
)

@Parcelize
data class PreviewSellerProduct(
    val token: String? = null,
    val imageUri: Uri? = null,
    val productName: String? = null,
    val categoryId: ArrayList<Int>?,
    val categoryName: ArrayList<String>?,
    val basePrice: String? = null,
    val productDescription: String? = null,
    val sellerImgUrl: String? = null,
    val sellerName: String? = null,
    val sellerLocation: String? = null
): Parcelable