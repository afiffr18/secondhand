package com.and2t2.secondhand.ui.uinotifikasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.domain.repository.NotifikasiRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class NotifikasiViewModel(private val notifikasiRepo: NotifikasiRepo) : ViewModel() {

    fun  getNotifikasi(access_token: String) = notifikasiRepo
        .getNotif(access_token)
        .asLiveData()

    fun updateNotifikasiRead(access_token : String,id : Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try{
            emit(Resource.success(notifikasiRepo.updateNotifikasiRead(access_token, id)))
        }catch (ex : IOException){
            emit(Resource.error(null,ex.message.toString()))
        }catch (ex : HttpException){
            emit(Resource.error(null,ex.message.toString()))
        }catch (ex : Exception){
            emit(Resource.error(null,ex.message.toString()))
        }

    }


}