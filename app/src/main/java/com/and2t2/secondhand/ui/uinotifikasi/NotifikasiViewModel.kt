package com.and2t2.secondhand.ui.uinotifikasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.and2t2.secondhand.domain.repository.NotifikasiRepo
import kotlinx.coroutines.launch


class NotifikasiViewModel(private val notifikasiRepo: NotifikasiRepo) : ViewModel() {

    fun  getNotifikasi(access_token: String) = notifikasiRepo
        .getNotif(access_token)
        .asLiveData()

    fun updateNotifikasiRead(access_token : String,id : Int) {
        viewModelScope.launch {
            notifikasiRepo.updateNotifikasiRead(access_token, id)
        }
    }
}