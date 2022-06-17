package com.and2t2.secondhand.domain.repository

import com.and2t2.secondhand.data.remote.dto.AuthService
import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginBody

class AuthRepo(private val authService: AuthService) {
    suspend fun postLogin(authLoginBody: AuthLoginBody) = authService.postLogin(authLoginBody)
}