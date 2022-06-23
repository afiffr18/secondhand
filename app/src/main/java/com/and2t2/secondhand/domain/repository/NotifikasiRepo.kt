package com.and2t2.secondhand.domain.repository

import androidx.room.withTransaction
import com.and2t2.secondhand.common.networkBoundResource
import com.and2t2.secondhand.data.local.DatabaseSecondHand
//import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.NotifikasiService
import com.and2t2.secondhand.domain.model.Notifikasi
import com.and2t2.secondhand.domain.model.NotifikasiMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotifikasiRepo(
    private val apiService : NotifikasiService,
    private val mapper: NotifikasiMapper,
    private val mDb : DatabaseSecondHand
) {
    suspend fun getNotifikasi(access_token : String) : List<Notifikasi>{
        val result = apiService.getNotification(access_token)
        return mapper.toDomainList(result)
    }

    private val notifikasiDao = mDb.notifikasiDao()

    fun getNotif(access_token: String) = networkBoundResource(
        query = {
                notifikasiDao.getNotifikasi()
            },
        fetch = {
                getNotifikasi(access_token)
            },
        saveFetchResult = { notif ->
                mDb.withTransaction{
                    notifikasiDao.deleteNotifikasi()
                    notifikasiDao.insertNotifikasi(notif)
                }

        }
      )

    suspend fun updateNotifikasiRead(access_token: String, id : Int) = withContext(Dispatchers.IO){
        apiService.updateNotificationRead(access_token,id)
    }


}