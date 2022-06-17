package com.and2t2.secondhand.data.remote.dto

import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginBody
import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginDtoItem
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun postLogin(
        @Body authLoginBody: AuthLoginBody
    ): AuthLoginDtoItem
}