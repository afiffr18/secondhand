package com.and2t2.secondhand.domain.repository

import androidx.room.withTransaction
import com.and2t2.secondhand.common.networkBoundResource
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.AuthService
import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginBody
import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginDtoItem
import com.and2t2.secondhand.domain.model.AuthUser
import com.and2t2.secondhand.domain.model.AuthUserMapper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class AuthRepo(
    private val authService: AuthService,
    private val mapper: AuthUserMapper,
    private val mDb: DatabaseSecondHand
) {
    suspend fun postLogin(authLoginBody: AuthLoginBody) : Response<AuthLoginDtoItem> {
        return authService.postLogin(authLoginBody)
    }

    suspend fun postRegister(fullName: RequestBody,
                             email: RequestBody,
                             password: RequestBody,
                             phoneNumber: RequestBody,
                             address: RequestBody,
                             city: RequestBody
    ) = authService.postRegister(fullName, email, password, phoneNumber, address, city, null)

    suspend fun getUserByToken(access_token: String) : AuthUser {
        val result = authService.getUser(access_token)
        return mapper.mapToDomainModel(result)
    }

    fun getUser(access_token: String) = networkBoundResource(
        query = {
                mDb.authDao().getUserDetail()
        },
        fetch = {
                getUserByToken(access_token)
        },
        saveFetchResult = { user ->
            mDb.withTransaction {
                mDb.authDao().deleteUserDetail()
                mDb.authDao().insertUserDetail(user)
            }
        }
    )

    suspend fun updateUser(access_token: String,
                           fullName: RequestBody,
                           phoneNumber: RequestBody,
                           address: RequestBody,
                           city: RequestBody,
                           image: MultipartBody.Part?
    ) = authService.updateUser(access_token, fullName, phoneNumber, address, city, image)
}