package com.and2t2.secondhand.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.and2t2.secondhand.domain.model.AuthUser
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthDao {
    @Query("SELECT * FROM AuthUser")
    fun getUserDetail(): Flow<AuthUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserDetail(authUser: AuthUser)

    @Query("DELETE FROM AuthUser")
    fun deleteUserDetail()
}