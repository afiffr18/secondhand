package com.and2t2.secondhand.ui.uiseller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.and2t2.secondhand.domain.repository.SellerRepo

class SellerCategoryViewModel(private val sellerRepo: SellerRepo): ViewModel() {

    fun getCategory() = sellerRepo.getAllCategory().asLiveData()

}