package com.and2t2.secondhand.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.and2t2.secondhand.domain.model.BuyerProduct
import com.and2t2.secondhand.domain.model.BuyerProductDetail
import com.and2t2.secondhand.domain.model.Notifikasi
import com.and2t2.secondhand.domain.model.SellerCategory

@Database(entities = [Notifikasi::class,BuyerProductDetail::class,BuyerProduct::class,SellerCategory::class], version = 7)
abstract class DatabaseSecondHand : RoomDatabase() {
    abstract fun notifikasiDao(): NotifikasiDao
    abstract fun buyerDao() : BuyerDao
    abstract fun sellerDao() : SellerDao
    companion object {
        private var INSTANCE: DatabaseSecondHand? = null

        fun getInstance(context: Context): DatabaseSecondHand? {
            if (INSTANCE == null) {
                synchronized(DatabaseSecondHand::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseSecondHand::class.java, "Notifikasi.db"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }
}