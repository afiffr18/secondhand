package com.and2t2.secondhand.domain.repository

import androidx.room.withTransaction
import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.common.networkBoundResource
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.BuyerService
import com.and2t2.secondhand.domain.model.BuyerProduct
import com.and2t2.secondhand.domain.model.BuyerProductMapper

class BuyerRepo(
    private val apiService: BuyerService,
    private val mapper: BuyerProductMapper,
    private val mDb : DatabaseSecondHand
) {

    suspend fun getBuyerProductById(id : Int) : BuyerProduct{
        val result = apiService.getBuyerProductById(id)
        return mapper.mapToDomainModel(result)
    }


    fun getProductDetail(id: Int) = networkBoundResource(
        query = {
            mDb.buyerDao().getProductDetail()
        },
        fetch = {
            getBuyerProductById(id)
        },
        saveFetchResult = { product ->
            mDb.withTransaction {
                mDb.buyerDao().deleteProductDetail()
                mDb.buyerDao().insertProductDetail(product)
            }
        }
    )
}