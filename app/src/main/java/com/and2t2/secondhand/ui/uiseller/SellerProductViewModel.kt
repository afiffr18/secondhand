package com.and2t2.secondhand.ui.uiseller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.and2t2.secondhand.domain.repository.SellerRepo

class SellerProductViewModel(private val sellerRepo: SellerRepo): ViewModel() {
    fun getAllProduct(accessToken: String) = sellerRepo.getAllProduct(accessToken).asLiveData()

    fun getAllOrder(accessToken: String,status: String) = sellerRepo.getAllOrder(accessToken,status).asLiveData()
}