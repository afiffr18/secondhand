package com.and2t2.secondhand.ui.uiseller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.domain.repository.SellerRepo
import kotlinx.coroutines.Dispatchers

class SellerProductViewModel(private val sellerRepo: SellerRepo): ViewModel() {
    fun getAllProduct(accessToken: String) = sellerRepo.getAllProduct(accessToken).asLiveData()
    fun getProductById(accessToken: String, productId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(sellerRepo.getSellerProductById(accessToken, productId)))
            Log.d("GET DETAIL RESPONSE", "GET DETAIL PRODUK SUKSES")
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error Occurred!"))
            Log.d("GET DETAIL RESPONSE", e.message.toString())
        }
    }
}