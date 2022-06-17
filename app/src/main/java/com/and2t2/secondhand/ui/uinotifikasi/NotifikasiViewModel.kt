package com.and2t2.secondhand.ui.uinotifikasi

import android.util.Log
import androidx.lifecycle.*
import com.and2t2.secondhand.domain.model.Notifikasi
import com.and2t2.secondhand.domain.repository.NotifikasiRepo
import id.afif.binarchallenge8.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class NotifikasiViewModel(private val notifikasiRepo: NotifikasiRepo) : ViewModel() {

    fun getNotifikasi() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.succes(notifikasiRepo.getNotifikasi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImpvaG5kb2VAbWFpbC5jb20iLCJpYXQiOjE2NTUyNzkxMjZ9.rQs7fzE9b3IbnbaadoSzzVpcYPj4skUs2cFa4727edU")))
        }catch (ex : IOException){
            emit(Resource.error(null,ex.message ?: "Error Occurred"))
        }
    }
}