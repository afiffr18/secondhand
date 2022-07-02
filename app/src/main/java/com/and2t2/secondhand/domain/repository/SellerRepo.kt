package com.and2t2.secondhand.domain.repository

import androidx.room.withTransaction
import com.and2t2.secondhand.common.networkBoundResource
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.SellerService
import com.and2t2.secondhand.data.remote.dto.seller.SellerProductDtoDelete
import com.and2t2.secondhand.data.remote.dto.seller.SellerProductDtoItem
import com.and2t2.secondhand.domain.model.SellerProduct
import com.and2t2.secondhand.domain.model.SellerProductMapper
import retrofit2.Response

class SellerRepo(
    private val sellerService: SellerService,
    private val mapper: SellerProductMapper,
    private val mDb: DatabaseSecondHand
) {
    suspend fun getProduct(accessToken: String): List<SellerProduct> {
        val result = sellerService.getSellerProduct(accessToken)
        return mapper.toDomainList(result)
    }

    private val sellerDao = mDb.sellerDao()

    fun getAllProduct(accessToken: String) = networkBoundResource(
        query = { sellerDao.getProductDetail() },
        fetch = { getProduct(accessToken) },
        saveFetchResult = { sellerProduct ->
            mDb.withTransaction {
                sellerDao.deleteProductDetail()
                sellerDao.insertProductDetail(sellerProduct)
            }
        }
    )

    suspend fun getSellerProductById(accessToken: String, productId: Int) : Response<SellerProductDtoItem> {
        return sellerService.getSellerProductById(accessToken, productId)
    }

    suspend fun deleteSellerProductById(accessToken: String, productId: Int) : Response<SellerProductDtoDelete> {
        return sellerService.deleteSellerProduct(accessToken, productId)
    }
}