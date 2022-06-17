package com.and2t2.secondhand.ui.uinotifikasi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.and2t2.secondhand.domain.model.Notifikasi
import com.and2t2.secondhand.domain.repository.NotifikasiRepo
import kotlinx.coroutines.launch
import java.io.IOException

class NotifikasiViewModel(private val notifikasiRepo: NotifikasiRepo) : ViewModel() {
    private val _dataNotifikasi = MutableLiveData<List<Notifikasi>>()
    val dataNotifikasi : LiveData<List<Notifikasi>> get() = _dataNotifikasi

    init {
        getNotifikasi()
    }
    fun getNotifikasi(){
        viewModelScope.launch {
            try {
                val result = notifikasiRepo.getNotifikasi("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImpvaG5kb2VAbWFpbC5jb20iLCJpYXQiOjE2NTUyNzkxMjZ9.rQs7fzE9b3IbnbaadoSzzVpcYPj4skUs2cFa4727edU")
                _dataNotifikasi.postValue(result)
            }catch (ex : IOException){
                Log.e("ViewModel",ex.message.toString())
            }
        }
    }
}