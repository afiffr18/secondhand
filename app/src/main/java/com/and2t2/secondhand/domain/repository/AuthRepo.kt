package com.and2t2.secondhand.domain.repository

import com.and2t2.secondhand.data.remote.dto.LoginService
import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginBody

class AuthRepo(private val loginService: LoginService) {
    suspend fun postLogin(authLoginBody: AuthLoginBody) = loginService.postLogin(authLoginBody)
}