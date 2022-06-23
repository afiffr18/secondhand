package com.and2t2.secondhand.data.remote.dto

import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginBody
import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginDtoItem
import com.and2t2.secondhand.data.remote.dto.auth.AuthUserDtoItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface AuthService {
    @POST("auth/login")
    suspend fun postLogin(
        @Body authLoginBody: AuthLoginBody
    ): AuthLoginDtoItem

    @Multipart
    @POST("auth/register")
    suspend fun postRegister(
        @Part("full_name") fullName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part("address") address: RequestBody,
        @Part("city") city: RequestBody,
        @Part image: MultipartBody.Part?
    ): AuthUserDtoItem
}