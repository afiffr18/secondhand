package com.and2t2.secondhand.domain.repository

import androidx.room.withTransaction
import com.and2t2.secondhand.common.networkBoundResource
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.SellerService
import com.and2t2.secondhand.data.remote.dto.seller.*
import com.and2t2.secondhand.domain.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class SellerRepo(
    private val sellerService: SellerService,
    private val mapper: SellerProductMapper,
    private val mapperCategory: SellerCategoryMapper,
    private val mapperOrder: SellerOrderMapper,
    private val mDb: DatabaseSecondHand
) {
    private val sellerDao = mDb.sellerDao()

//    suspend fun getProduct(accessToken: String): List<SellerProduct> {
//        val result = sellerService.getSellerProduct(accessToken)
//        return mapper.toDomainList(result)
//    }
//
//
//    fun getAllProduct(accessToken: String) = networkBoundResource(
//        query = { sellerDao.getProductDetail() },
//        fetch = { getProduct(accessToken) },
//        saveFetchResult = { sellerProduct ->
//            mDb.withTransaction {
//                sellerDao.deleteProductDetail()
//                sellerDao.insertProductDetail(sellerProduct)
//            }
//        }
//    )

    suspend fun getSellerProduct(accessToken: String) : Response<SellerProductDto> {
        return sellerService.getSellerProduct(accessToken)
    }

    suspend fun getSellerProductById(accessToken: String, productId: Int) : Response<SellerProductDtoItem> {
        return sellerService.getSellerProductById(accessToken, productId)
    }

    suspend fun deleteSellerProductById(accessToken: String, productId: Int) : Response<SellerProductDtoDelete> {
        return sellerService.deleteSellerProduct(accessToken, productId)
    }

    suspend fun updateSellerProductById(
        access_token: String,
        productId: Int,
        name: RequestBody,
        description: RequestBody,
        basePrice: RequestBody,
        categoryIds: RequestBody,
        location: RequestBody,
        image: MultipartBody.Part?
    ) : Response<SellerProductDtoPutPost> {
        return sellerService.updateSellerProduct(access_token, productId, name, description, basePrice, categoryIds, location, image)
    }

    suspend fun getCategory(): List<SellerCategory> {
        val result = sellerService.getSellerCategory()
        return mapperCategory.toDomainList(result)
    }

    fun getAllCategory() = networkBoundResource(
        query = { sellerDao.getSellerCategory() },
        fetch = { getCategory() },
        saveFetchResult = { category ->
            mDb.withTransaction {
                sellerDao.deleteSellerCategory()
                sellerDao.insertSellerCategory(category)
            }
        }
    )

    suspend fun postProduct(access_token: String,
                            name: RequestBody,
                            description: RequestBody,
                            basePrice: RequestBody,
                            categoryIds: RequestBody,
                            location: RequestBody,
                            image: MultipartBody.Part?
    ) : Response<SellerProductDtoPutPost> {
        return sellerService.setSellerProduct(access_token, name, description, basePrice, categoryIds, location, image)
    }

    /*** Seller Order ***/
    suspend fun getSellerOrder(accessToken: String,status : String?) : List<SellerOrder>{
        val result = sellerService.getSellerOrder(accessToken,status)
        return mapperOrder.toDomainList(result)
    }

    suspend fun getSelletOrderById(accessToken: String,id: Int) : SellerOrder{
        val result = sellerService.getSellerOrderById(accessToken,id)
        return mapperOrder.mapToDomainModel(result)
    }

    suspend fun updateSelerOrderStatus(accessToken: String,id : Int,status: String?) : SellerOrderStatusDto{
        return sellerService.updateSellerOrderStatus(accessToken,id, status)
    }


}