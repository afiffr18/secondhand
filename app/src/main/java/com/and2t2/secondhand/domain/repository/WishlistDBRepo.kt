package com.and2t2.secondhand.domain.repository

import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.local.WishlistId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WishlistDBRepo(private val mDb : DatabaseSecondHand) {

    suspend fun getWishlistIdfromDb(productId : Int) = withContext(Dispatchers.IO){
        mDb.wishlistDao().exists(productId)
    }

    suspend fun deleteWishlistid(wishlistId: WishlistId) = withContext(Dispatchers.IO){
        mDb.wishlistDao().deleteWishlistid(wishlistId)
    }

    suspend fun insertWishlistid(wishlistId: WishlistId) = withContext(Dispatchers.IO){
        mDb.wishlistDao().insertWishlistId(wishlistId)
    }
}