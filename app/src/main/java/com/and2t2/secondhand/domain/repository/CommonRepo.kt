package com.and2t2.secondhand.domain.repository

import android.content.Context
import androidx.room.withTransaction
import com.and2t2.secondhand.data.local.DatabaseSecondHand

class CommonRepo(context: Context) {

    private val mDb = DatabaseSecondHand.getInstance(context)

    suspend fun deleteTable(){
        mDb?.withTransaction {
            mDb.sellerDao().deleteProductDetail()
            mDb.notifikasiDao().deleteNotifikasi()
            mDb.authDao().deleteUserDetail()
            mDb.wishlistDao().deleteWishlist()
        }
    }
}