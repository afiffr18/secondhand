package com.and2t2.secondhand.ui.uihome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.domain.repository.HomeRepo
import kotlinx.coroutines.Dispatchers
import java.io.IOException
import java.lang.Exception

class HomeViewModel(private val homeRepo: HomeRepo) : ViewModel() {

//    fun getKategori() = liveData(Dispatchers.IO) {
//        emit(Resource.loading(null))
//        try {
//            emit(Resource.success(homeRepo.getKategori()))
//        }catch (ex : Exception){
//            emit(Resource.error(null,ex.message.toString()))
//        }catch (ex : IOException){
//            emit(Resource.error(null,ex.message.toString()))
//        }
//    }

    fun getKategori() = homeRepo.getSellerKategori().asLiveData()

    fun getBuyerProduct(status : String?,categoryId : Int?,search : String?)
    = homeRepo.getBuyerProduct(status, categoryId, search).asLiveData()
}