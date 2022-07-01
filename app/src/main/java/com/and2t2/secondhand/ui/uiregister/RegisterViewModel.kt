package com.and2t2.secondhand.ui.uiregister

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.domain.repository.AuthRepo
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RegisterViewModel(private val authRepo: AuthRepo): ViewModel() {
    fun doRegister(fullName: RequestBody,
                   email: RequestBody,
                   password: RequestBody,
                   phoneNumber: RequestBody,
                   address: RequestBody,
                   city: RequestBody,
                   image: MultipartBody.Part?
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(authRepo.postRegister(fullName, email, password, phoneNumber, address, city, image)))
            Log.d("REGISTER RESPONSE", "REGISTER SUKSES")
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
            Log.d("REGISTER RESPONSE", e.message.toString())
        }
    }
}