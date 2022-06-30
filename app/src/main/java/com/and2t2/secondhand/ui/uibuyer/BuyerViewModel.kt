package com.and2t2.secondhand.ui.uibuyer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.data.remote.dto.buyer.PostBuyerOrderBody
import com.and2t2.secondhand.domain.repository.BuyerRepo
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException


class BuyerViewModel(private val buyerRepo: BuyerRepo) : ViewModel() {

    fun getProductDetail(id: Int) = buyerRepo.getProductDetail(id).asLiveData()

    fun setBuyerOrder(postBuyerOrderBody: PostBuyerOrderBody) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            val response = buyerRepo.setBuyerOrder(postBuyerOrderBody)
            if(response.isSuccessful){
                emit(Resource.success(response.body()))
            }else{
                val errorMsg = response.errorBody()?.string()
                response.errorBody()?.close()
                emit(Resource.error(null,errorMsg.toString()))
            }
        } catch (ex : HttpException){
            emit(Resource.error(null,ex.message() ?: "Womething went wrong"))
        } catch (ex : IOException){
            emit(Resource.error(null,"Please check your network connection"))
        } catch (ex : Exception){
            emit(Resource.error(null,"Something went wrong"))
        }

    }


}