package com.and2t2.secondhand.ui.uibuyer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.and2t2.secondhand.domain.repository.BuyerRepo


class BuyerViewModel(private val buyerRepo: BuyerRepo) : ViewModel() {
    fun getProductDetail(id: Int) = buyerRepo.getProductDetail(id).asLiveData()
}