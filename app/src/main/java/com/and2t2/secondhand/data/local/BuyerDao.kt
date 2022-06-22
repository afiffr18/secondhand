package com.and2t2.secondhand.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.and2t2.secondhand.domain.model.BuyerProduct
import com.and2t2.secondhand.domain.model.Notifikasi
import kotlinx.coroutines.flow.Flow

@Dao
interface BuyerDao {
    @Query("SELECT * FROM BuyerProduct")
    fun getProductDetail() : Flow<BuyerProduct>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductDetail(buyerProduct: BuyerProduct)

    @Query("DELETE FROM BuyerProduct")
    fun deleteProductDetail()
}