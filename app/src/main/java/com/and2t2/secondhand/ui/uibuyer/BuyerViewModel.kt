package com.and2t2.secondhand.ui.uibuyer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.and2t2.secondhand.domain.repository.BuyerRepo
import id.afif.binarchallenge8.domain.util.Resource
import kotlinx.coroutines.delay
import java.io.IOException

class BuyerViewModel(private val buyerRepo: BuyerRepo) : ViewModel() {

    fun getProductDetail(id: Int) = buyerRepo.getProductDetail(id).asLiveData()
}