package com.and2t2.secondhand.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.and2t2.secondhand.domain.model.BuyerProduct
import com.and2t2.secondhand.domain.model.BuyerProductDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface BuyerDao {
    @Query("SELECT * FROM BuyerProductDetail")
    fun getProductDetail() : Flow<BuyerProductDetail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductDetail(buyerProductDetail: BuyerProductDetail)

    @Query("DELETE FROM BuyerProductDetail")
    fun deleteProductDetail()

    @Query("SELECT * FROM BuyerProduct")
    fun getProduct() : Flow<List<BuyerProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(buyerProduct: List<BuyerProduct>)

    @Query("DELETE FROM BuyerProduct")
    fun deleteProduct()
}