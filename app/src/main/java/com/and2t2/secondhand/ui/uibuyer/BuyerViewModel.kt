package com.and2t2.secondhand.ui.uibuyer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.domain.repository.BuyerRepo
//import id.afif.binarchallenge8.domain.util.Resource
import kotlinx.coroutines.delay
import java.io.IOException

class BuyerViewModel(private val buyerRepo: BuyerRepo) : ViewModel() {

     fun getProductById(id : Int) = liveData {
        emit(Resource.loading(null))
        delay(2000)
        try {
            emit(Resource.success(buyerRepo.getBuyerProductById(id)))
        }catch (ex : IOException){
            emit(Resource.error(null,ex.message.toString()))
        }
    }
}