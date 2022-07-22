package com.and2t2.secondhand.data.local

import androidx.room.*

@Dao
interface WishlistDao {

    @Query("SELECT EXISTS (SELECT 1 FROM WishlistId WHERE productId = :id)")
    fun exists(id: Int): Boolean

    @Delete
    fun deleteWishlistid(wishlistId: WishlistId) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWishlistId(wishlistId: WishlistId)
}