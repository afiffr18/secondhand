package com.and2t2.secondhand.ui.uiwishlist

import androidx.lifecycle.*
import com.and2t2.secondhand.data.local.WishlistId
import com.and2t2.secondhand.domain.repository.WishlistDBRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WishlistDBViewModel(private val wishlistDBRepo: WishlistDBRepo) :ViewModel() {
    private var _inWishlist = MutableLiveData<Boolean>()
    val inWishlistId : LiveData<Boolean> get() = _inWishlist

    fun getWishlistId(productId : Int){
        viewModelScope.launch {
           val result = wishlistDBRepo.getWishlistIdfromDb(productId)
            _inWishlist.postValue(result)
        }
    }

    fun deleteWishlistid(wishlistId: WishlistId){
        viewModelScope.launch {
            wishlistDBRepo.deleteWishlistid(wishlistId)
        }
    }

    fun insertWishlist(wishlistId: WishlistId){
        viewModelScope.launch {
            wishlistDBRepo.insertWishlistid(wishlistId)
        }
    }
}