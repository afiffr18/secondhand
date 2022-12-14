package com.and2t2.secondhand.data.remote

import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginBody
import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginDtoItem
import com.and2t2.secondhand.data.remote.dto.auth.AuthUserDtoItem
import com.and2t2.secondhand.data.remote.dto.auth.AuthUserError
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AuthService {
    @POST("auth/login")
    suspend fun postLogin(
        @Body authLoginBody: AuthLoginBody
    ): Response<AuthLoginDtoItem>

    @Multipart
    @POST("auth/register")
    suspend fun postRegister(
        @Part("full_name") fullName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<AuthUserDtoItem>

    @GET("auth/user")
    suspend fun getUser(
        @Header("access_token") accessToken: String
    ): AuthUserDtoItem

    @Multipart
    @PUT("auth/user")
    suspend fun updateUser(
        @Header("access_token") accessToken: String,
        @Part("full_name") fullName: RequestBody?,
        @Part("phone_number") phoneNumber: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<AuthUserDtoItem>

    @Multipart
    @PUT("auth/change-password")
    suspend fun changePassword(
        @Header("access_token") accessToken: String,
        @Part("current_password") currentPassword: RequestBody,
        @Part("new_password") newPassword: RequestBody,
        @Part("confirm_password") confirmPassword: RequestBody,
    ): Response<AuthUserError>
}