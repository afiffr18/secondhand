package com.and2t2.secondhand.ui.uiseller.uiinfopenawar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.domain.repository.SellerRepo
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException

class InfoPenawarViewModel(private val sellerRepo: SellerRepo) : ViewModel() {

    fun getSellerOrder(acccess_token : String,status : String?) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try{
            emit(Resource.success(sellerRepo.getSellerOrder(acccess_token,status)))
        }catch (ex : IOException){
            emit(Resource.error(null,ex.message.toString()))
        }catch (ex : HttpException){
            emit(Resource.error(null,ex.message.toString()))
        }catch (ex : Exception){
            emit(Resource.error(null,ex.message.toString()))
        }
    }

    fun getSellerOrderById(acccess_token : String,id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try{
            emit(Resource.success(sellerRepo.getSelletOrderById(acccess_token,id)))
        }catch (ex : IOException){
            emit(Resource.error(null,ex.message.toString()))
        }catch (ex : HttpException){
            emit(Resource.error(null,ex.message.toString()))
        }catch (ex : Exception){
            emit(Resource.error(null,ex.message.toString()))
        }
    }

    fun updateSellerOrderStatus(access_token : String,id : Int,status: String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(sellerRepo.updateSelerOrderStatus(access_token,id, status)))
        }catch (ex : IOException){
            emit(Resource.error(null,ex.message.toString()))
        }catch (ex : HttpException){
            emit(Resource.error(null,ex.message.toString()))
        }catch (ex : Exception){
            emit(Resource.error(null,ex.message.toString()))
        }
    }
}