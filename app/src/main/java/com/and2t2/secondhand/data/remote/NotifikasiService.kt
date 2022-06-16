package com.and2t2.secondhand.data.remote

import com.and2t2.secondhand.data.remote.dto.notification.NotificationDto
import com.and2t2.secondhand.data.remote.dto.notification.NotificationDtoItem
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface NotifikasiService {

    @GET("notification")
    suspend fun getNotification(@Header("access_token") token : String) : NotificationDto

    @GET("notification/{id}")
    suspend fun getNotificationDetail(
        @Header("access_token")token: String,
        @Path("id") id : Int
    ) : NotificationDtoItem
}