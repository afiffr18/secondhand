package com.and2t2.secondhand.ui.uiseller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.and2t2.secondhand.domain.repository.SellerRepo
import com.bumptech.glide.load.engine.Resource
import okhttp3.Dispatcher
import okhttp3.MultipartBody
import okhttp3.RequestBody

class  DetailProdukViewModel(private val sellerRepo: SellerRepo): ViewModel() {

//    fun addProduct(access_token: String,
//                   name: RequestBody,
//                   basePrice: RequestBody,
//                   categoryIds: RequestBody,
//                   description: RequestBody,
//                   image: MultipartBody.Part?
//    ) = liveData(Dispatcher.IO) {
//        emit(resource.loading(null))
//        try {
//            emit(Resource.success(sellerRepo.getProduct(access_token,)))
//        }catch (e: Exception) {
//            emit(com.and2t2.secondhand.common.Resource.error(data = null, message = e.message ?: "Error Occurred!"))
//            Log.d("UPDATE RESPONSE", e.message.toString())
//        }
//    }
}