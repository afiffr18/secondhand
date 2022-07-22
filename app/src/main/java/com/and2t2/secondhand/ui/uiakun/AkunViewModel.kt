package com.and2t2.secondhand.ui.uiakun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.and2t2.secondhand.domain.repository.CommonRepo
import com.and2t2.secondhand.domain.repository.NotifikasiRepo
import kotlinx.coroutines.launch

class AkunViewModel(private val commonRepo: CommonRepo) : ViewModel() {
    fun deleteTable(){
        viewModelScope.launch {
            commonRepo.deleteTable()
        }
    }
}