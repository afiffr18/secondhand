package com.and2t2.secondhand.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.and2t2.secondhand.domain.model.SellerCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface SellerDao {
    @Query("SELECT * FROM SellerCategory ORDER by id ASC")
    fun getSellerCategory() : Flow<List<SellerCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSellerCategory(sellerCategory: List<SellerCategory>)

    @Query("DELETE FROM SellerCategory")
    fun deleteSellerCategory()
}