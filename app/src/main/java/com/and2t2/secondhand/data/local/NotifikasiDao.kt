package com.and2t2.secondhand.data.local

import androidx.room.*
import com.and2t2.secondhand.domain.model.Notifikasi
import kotlinx.coroutines.flow.Flow

@Dao
interface NotifikasiDao {

    @Query("SELECT * FROM Notifikasi")
    fun getNotifikasi() : Flow<List<Notifikasi>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifikasi(notifikasi : List<Notifikasi>)

    @Query("DELETE FROM Notifikasi")
    suspend fun deleteNotifikasi()

}