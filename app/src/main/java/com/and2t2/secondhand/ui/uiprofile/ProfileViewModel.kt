package com.and2t2.secondhand.ui.uiprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.data.remote.dto.auth.AuthUserError
import com.and2t2.secondhand.domain.repository.AuthRepo
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class ProfileViewModel(private val authRepo: AuthRepo): ViewModel() {
    fun getUser(access_token: String) = authRepo.getUser(access_token).asLiveData()

    fun doUpdateUser(access_token: String,
                     fullName: RequestBody?,
                     phoneNumber: RequestBody?,
                     address: RequestBody?,
                     city: RequestBody?,
                     image: MultipartBody.Part?
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = authRepo.updateUser(access_token, fullName, phoneNumber, address, city, image)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
                Log.d("UPDATE USER RESPONSE", "UPDATE USER SUKSES")
            } else {
                val gson = Gson()
                val errorMessage = response.errorBody()?.string()
                val data = gson.fromJson(errorMessage, AuthUserError::class.java)
                response.errorBody()?.close()
                emit(Resource.error(null, data.message))
                Log.d("UPDATE USER RESPONSE", "UPDATE USER GAGAL")
            }
        } catch (e: HttpException) {
            emit(Resource.error(null, "Something went wrong"))
        } catch (e: IOException) {
            emit(Resource.error(null, "Please check your network connection"))
        } catch (e: Exception) {
            emit(Resource.error(null, "Something went wrong"))
        }
    }
}