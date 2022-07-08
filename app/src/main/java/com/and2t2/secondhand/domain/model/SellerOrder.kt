package com.and2t2.secondhand.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.and2t2.secondhand.data.remote.dto.seller.User

@Entity
data class SellerOrder(
    @PrimaryKey val id: Int,
    @ColumnInfo (name = "product_id") val productId: Int? = null,
    @ColumnInfo (name = "buyer_id") val buyerId: Int? = null,
    @ColumnInfo (name = "price") val price: Int? = null,
    @ColumnInfo (name = "product_name") val productName: String? = null,
    @ColumnInfo (name = "base_price") val basePrice: Int? = null,
    @ColumnInfo (name = "image_product") val imageProduct: String? = null,
    @ColumnInfo (name = "status") val status: String? = null,
    @ColumnInfo (name = "updated_at") val updatedAt: String? = null,
    @ColumnInfo (name = "createdAt") val createdAt: String? = null,
    @ColumnInfo (name = "Product") val Product: String? = null,
    @ColumnInfo (name = "transaction_date") val transactionDate: Int? = null,
//    @ColumnInfo (name = "user") val User: User? = null
)

