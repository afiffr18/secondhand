package com.and2t2.secondhand.ui.uilogin

import android.util.Log
import androidx.lifecycle.*
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.data.remote.dto.auth.AuthLoginBody
import com.and2t2.secondhand.domain.repository.AuthRepo
import kotlinx.coroutines.Dispatchers

class LoginViewModel(private val authRepo: AuthRepo): ViewModel() {
    fun doLogin(authLoginBody: AuthLoginBody) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(authRepo.postLogin(authLoginBody)))
            Log.d("LOGIN RESPONSE", "LOGIN SUKSES")
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
            Log.d("LOGIN RESPONSE", "LOGIN GAGAL")
        }
    }
}