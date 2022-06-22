package com.and2t2.secondhand.ui.uinotifikasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.and2t2.secondhand.domain.repository.NotifikasiRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotifikasiViewModel(private val notifikasiRepo: NotifikasiRepo) : ViewModel() {

    val notifikasi = notifikasiRepo
        .getNotif("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImFmaWZAbWFpbC5jb20iLCJpYXQiOjE2NTU0NzkxMzd9.NEn3MajCccdWpLkHiAFAhez3DaFEPIdor7-MDxG9HoE")
        .asLiveData()

    fun updateNotifikasiRead(access_token : String,id : Int) {
        viewModelScope.launch {
            notifikasiRepo.updateNotifikasiRead(access_token, id)
        }
    }
}