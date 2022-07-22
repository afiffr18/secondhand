package com.and2t2.secondhand.ui.uiwishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.data.remote.dto.wishlist.PostWishlistBody
import com.and2t2.secondhand.data.remote.dto.wishlist.PostWishlistDto
import com.and2t2.secondhand.data.remote.dto.wishlist.WishlistDeleteDto
import com.and2t2.secondhand.domain.repository.WishlistRepo
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException

class WishlistViewModel(val wishlistRepo: WishlistRepo) : ViewModel() {

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

    fun postBuyerWishlist(access_token: String,postWishlistBody: PostWishlistBody) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = wishlistRepo.postBuyerWishlist(access_token, postWishlistBody)
            if(response.isSuccessful){
                emit(Resource.success(response.body()?.product))
            }
        }catch (ex : IOException){
            emit(Resource.error(null,ex.message.toString()))
        }catch (ex : HttpException){
            emit(Resource.error(null,ex.message.toString()))
        }catch (ex : Exception){
            emit(Resource.error(null,ex.message.toString()))
        }
    }

    fun deleteWishlist(access_token: String,id : Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = wishlistRepo.deleteBuyerWishlist(access_token, id)
            if(response.isSuccessful){
                emit(Resource.success(response.body()))
            }else{
                val errorMessageinGson = response.errorBody().toString()
                val gson = Gson()
                val errorMessage = gson.fromJson(errorMessageinGson,WishlistDeleteDto::class.java)
                emit(Resource.error(null,errorMessage.message))
            }
        }catch (e: HttpException) {
            emit(Resource.error(null, "Something went wrong"))
        } catch (e: IOException) {
            emit(Resource.error(null, "Please check your network connection"))
        } catch (e: Exception) {
            emit(Resource.error(null, "Something went wrong"))
        }
    }
}