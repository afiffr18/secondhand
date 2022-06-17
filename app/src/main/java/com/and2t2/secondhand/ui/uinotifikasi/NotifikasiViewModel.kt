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
            emit(Resource.succes(notifikasiRepo.getNotifikasi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImFmaWZAbWFpbC5jb20iLCJpYXQiOjE2NTU0NzkxMzd9.NEn3MajCccdWpLkHiAFAhez3DaFEPIdor7-MDxG9HoE")))
        }catch (ex : IOException){
            emit(Resource.error(null,ex.message ?: "Error Occurred"))
        }
    }
}