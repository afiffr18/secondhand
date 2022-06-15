package com.and2t2.secondhand.domain.repository

import com.and2t2.secondhand.data.remote.NotifikasiService
import com.and2t2.secondhand.domain.model.Notifikasi
import com.and2t2.secondhand.domain.model.NotifikasiMapper

class NotifikasiRepo(
    private val apiService : NotifikasiService,
    private val mapper: NotifikasiMapper
) {
    suspend fun getNotifikasi(access_token : String) : List<Notifikasi>{
        val result = apiService.getNotification(access_token)
        return mapper.toDomainList(result)
    }
}