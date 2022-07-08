package com.and2t2.secondhand.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.and2t2.secondhand.domain.model.SellerOrder
import com.and2t2.secondhand.domain.model.SellerProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface SellerDao {
    @Query("SELECT * FROM SellerProduct")
    fun getProductDetail() : Flow<List<SellerProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductDetail(sellerProduct: List<SellerProduct>)

    @Query("DELETE FROM SellerProduct")
    fun deleteProductDetail()


    @Query("SELECT * FROM SellerOrder")
    fun getOrderDetail() : Flow<List<SellerOrder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderDetail(sellerProduct: List<SellerOrder>)

    @Query("DELETE FROM SellerOrder")
    fun deleteOrderDetail()
}