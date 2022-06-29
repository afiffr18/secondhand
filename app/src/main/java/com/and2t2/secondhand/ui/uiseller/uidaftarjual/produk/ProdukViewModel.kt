package com.and2t2.secondhand.ui.uiseller.uidaftarjual.produk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.domain.repository.SellerRepo
import kotlinx.coroutines.Dispatchers

class ProdukViewModel(private val sellerRepo: SellerRepo): ViewModel() {
    fun getAllProduct(accessToken: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(sellerRepo.getProduct(accessToken)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
        }
    }
}