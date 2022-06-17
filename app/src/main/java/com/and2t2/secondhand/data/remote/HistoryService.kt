package com.and2t2.secondhand.data.remote

import com.and2t2.secondhand.data.remote.dto.history.HistoryDto
import com.and2t2.secondhand.data.remote.dto.history.HistoryDtoItem
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface HistoryService {

    @GET("history")
    fun getHistory(
        @Header("access_token") token : String
    ) : HistoryDto

    @GET("history/{id}")
    fun getHistoryById(
        @Header("access_token") token : String,
        @Path("id") id : Int
    ) : HistoryDtoItem

}