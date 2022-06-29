package com.and2t2.secondhand.domain.repository

import androidx.room.withTransaction
import com.and2t2.secondhand.common.networkBoundResource
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.SellerService
import com.and2t2.secondhand.domain.model.SellerProduct
import com.and2t2.secondhand.domain.model.SellerProductMapper

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
}