package com.and2t2.secondhand.ui.uilogin

import android.util.Log
import androidx.lifecycle.*
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginBody
import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginError
import com.and2t2.secondhand.domain.repository.AuthRepo
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(private val authRepo: AuthRepo): ViewModel() {
    fun doLogin(authLoginBody: AuthLoginBody) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = authRepo.postLogin(authLoginBody)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
                Log.d("LOGIN RESPONSE", "LOGIN SUKSES")
            } else {
                val gson = Gson()
                val errorMessage = response.errorBody()?.string()
                val data = gson.fromJson(errorMessage, AuthLoginError::class.java)
                response.errorBody()?.close()
                emit(Resource.error(null, data.message))
                Log.d("LOGIN RESPONSE", "LOGIN GAGAL")
            }
        } catch (e: HttpException) {
            Resource.error(null, "Something went wrong")
        } catch (e: IOException) {
            Resource.error(null, "Please check your network connection")
        } catch (e: Exception) {
            Resource.error(null, "Something went wrong")
        }
    }
}