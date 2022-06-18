package com.and2t2.secondhand.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.and2t2.secondhand.domain.model.Notifikasi

@Database(entities = [Notifikasi::class], version = 1)
abstract class DatabaseNotifikasi : RoomDatabase() {
    abstract fun notifikasiDao(): NotifikasiDao
    companion object {
        private var INSTANCE: DatabaseNotifikasi? = null

        fun getInstance(context: Context): DatabaseNotifikasi? {
            if (INSTANCE == null) {
                synchronized(DatabaseNotifikasi::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseNotifikasi::class.java, "Notifikasi.db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }
}