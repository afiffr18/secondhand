package com.and2t2.secondhand.data.remote

import com.and2t2.secondhand.data.remote.dto.NotificationDto
import com.and2t2.secondhand.data.remote.dto.NotificationDtoItem
import retrofit2.http.GET
import retrofit2.http.Header

interface NotifikasiService {

    @GET("notification")
    suspend fun getNotification(@Header("access_token") token : String) : NotificationDto
}