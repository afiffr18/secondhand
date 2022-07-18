package com.and2t2.secondhand.ui.uiakun

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.domain.repository.WishlistRepo
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException

class WishlistViewModel(private val wishlistRepo: WishlistRepo) : ViewModel() {

    fun getBuyerWishlist(access_token : String) = liveData(Dispatchers.IO){
        emit(Resource.loading(null))
        try {
            emit(Resource.success(wishlistRepo.getBuyerWishlist(access_token)))
        }catch (ex : IOException){
            emit(Resource.error(null,ex.message.toString()))
        }catch (ex : HttpException){
            emit(Resource.error(null,ex.message.toString()))
        }catch (ex : Exception){
            emit(Resource.error(null,ex.message.toString()))
        }
    }
}