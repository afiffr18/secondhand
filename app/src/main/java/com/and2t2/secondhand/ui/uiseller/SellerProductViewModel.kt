package com.and2t2.secondhand.ui.uiseller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.and2t2.secondhand.common.Resource
import com.and2t2.secondhand.data.remote.dto.seller.SellerProductError
import com.and2t2.secondhand.domain.repository.SellerRepo
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class SellerProductViewModel(private val sellerRepo: SellerRepo): ViewModel() {
//    fun getAllProduct(accessToken: String) = sellerRepo.getAllProduct(accessToken).asLiveData()

    fun getAllProduct(accessToken: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = sellerRepo.getSellerProduct(accessToken)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
                Log.d("GET PRODUCT RESPONSE", "GET DETAIL PRODUK SUKSES")
            } else {
                val gson = Gson()
                val errorMessage = response.errorBody()?.string()
                val data = gson.fromJson(errorMessage, SellerProductError::class.java)
                response.errorBody()?.close()
                emit(Resource.error(null, data.message))
                Log.d("GET PRODUCT RESPONSE", "GET DETAIL PRODUK GAGAL")
            }
        } catch (e: HttpException) {
            emit(Resource.error(null, "Something went wrong"))
        } catch (e: IOException) {
            emit(Resource.error(null, "Please check your network connection"))
        } catch (e: Exception) {
            emit(Resource.error(null, "Something went wrong"))
        }
    }


    fun getProductById(accessToken: String, productId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = sellerRepo.getSellerProductById(accessToken, productId)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
                Log.d("GET DETAIL RESPONSE", "GET DETAIL PRODUK SUKSES")
            } else {
                val gson = Gson()
                val errorMessage = response.errorBody()?.string()
                val data = gson.fromJson(errorMessage, SellerProductError::class.java)
                response.errorBody()?.close()
                emit(Resource.error(null, data.message))
                Log.d("GET DETAIL RESPONSE", "GET DETAIL PRODUK GAGAL")
            }
        } catch (e: HttpException) {
            emit(Resource.error(null, "Something went wrong"))
        } catch (e: IOException) {
            emit(Resource.error(null, "Please check your network connection"))
        } catch (e: Exception) {
            emit(Resource.error(null, "Something went wrong"))
        }
    }

    fun deleteProductById(accessToken: String, productId: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = sellerRepo.deleteSellerProductById(accessToken, productId)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
                Log.d("DELETE PRODUCT RESPONSE", "DELETE PRODUCT SUKSES")
            } else {
                val gson = Gson()
                val errorMessage = response.errorBody()?.string()
                val data = gson.fromJson(errorMessage, SellerProductError::class.java)
                response.errorBody()?.close()
                emit(Resource.error(null, data.message))
                Log.d("DELETE PRODUCT RESPONSE", "DELETE PRODUCT GAGAL")
            }
        } catch (e: HttpException) {
            emit(Resource.error(null, "Something went wrong"))
        } catch (e: IOException) {
            emit(Resource.error(null, "Please check your network connection"))
        } catch (e: Exception) {
            emit(Resource.error(null, "Something went wrong"))
        }
    }

    fun postProduct(access_token: String,
                    name: RequestBody,
                    basePrice: RequestBody,
                    categoryIds: RequestBody,
                    description: RequestBody,
                    location: RequestBody,
                    image: MultipartBody.Part?
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = sellerRepo.postProduct(access_token, name, description, basePrice, categoryIds, location, image)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
                Log.d("POST PRODUCT RESPONSE", "PRODUCT BERHASIL DITAMBAHKAN")
            } else {
                val gson = Gson()
                val errorMessage = response.errorBody()?.string()
                val data = gson.fromJson(errorMessage, SellerProductError::class.java)
                response.errorBody()?.close()
                emit(Resource.error(null, data.message))
                Log.d("POST PRODUCT RESPONSE", "PRODUCT GAGAL DITAMBAHKAN")
            }
        } catch (e: HttpException) {
            emit(Resource.error(null, "Something went wrong"))
        } catch (e: IOException) {
            emit(Resource.error(null, "Please check your network connection"))
        } catch (e: Exception) {
            emit(Resource.error(null, "Something went wrong"))
        }
    }

    fun updateProductById(
        access_token: String,
        productId: Int,
        name: RequestBody,
        description: RequestBody,
        basePrice: RequestBody,
        categoryIds: RequestBody,
        location: RequestBody,
        image: MultipartBody.Part?
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            val response = sellerRepo.updateSellerProductById(access_token, productId, name, description, basePrice, categoryIds, location, image)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
                Log.d("UPDATE PRODUCT RESPONSE", "UPDATE PRODUCT SUKSES")
            } else {
                val gson = Gson()
                val errorMessage = response.errorBody()?.string()
                val data = gson.fromJson(errorMessage, SellerProductError::class.java)
                response.errorBody()?.close()
                emit(Resource.error(null, data.message))
                Log.d("UPDATE PRODUCT RESPONSE", "UPDATE PRODUCT GAGAL")
            }
        } catch (e: HttpException) {
            emit(Resource.error(null, "Something went wrong"))
        } catch (e: IOException) {
            emit(Resource.error(null, "Please check your network connection"))
        } catch (e: Exception) {
            emit(Resource.error(null, "Something went wrong"))
        }
    }
}