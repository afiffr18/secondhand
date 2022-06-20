package com.and2t2.secondhand.domain.repository

import com.and2t2.secondhand.data.remote.dto.AuthService
import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginBody
import okhttp3.RequestBody

class AuthRepo(private val authService: AuthService) {
    suspend fun postLogin(authLoginBody: AuthLoginBody) = authService.postLogin(authLoginBody)
    suspend fun postRegister(fullName: RequestBody,
                             email: RequestBody,
                             password: RequestBody,
                             phoneNumber: RequestBody,
                             address: RequestBody,
                             city: RequestBody
    ) = authService.postRegister(fullName, email, password, phoneNumber, address, city, null)
}