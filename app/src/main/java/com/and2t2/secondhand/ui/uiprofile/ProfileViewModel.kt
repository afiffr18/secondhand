package com.and2t2.secondhand.ui.uiprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.domain.repository.AuthRepo
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileViewModel(private val authRepo: AuthRepo): ViewModel() {
    fun getUser(access_token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(authRepo.getUser(access_token)))
            Log.d("GET USER RESPONSE", "GET USER SUKSES")
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
            Log.d("GET USER RESPONSE", e.message.toString())
        }
    }

    fun doUpdateUser(access_token: String,
                     fullName: RequestBody,
                     phoneNumber: RequestBody,
                     address: RequestBody,
                     city: RequestBody,
                     image: MultipartBody.Part?
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(authRepo.updateUser(access_token, fullName, phoneNumber, address, city, image)))
            Log.d("UPDATE RESPONSE", "UPDATE SUKSES")
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
            Log.d("UPDATE RESPONSE", e.message.toString())
        }
    }
}