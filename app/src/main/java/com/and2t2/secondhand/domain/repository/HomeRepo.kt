package com.and2t2.secondhand.domain.repository

import androidx.room.withTransaction
import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.common.networkBoundResource
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.BuyerService
import com.and2t2.secondhand.data.remote.SellerService
import com.and2t2.secondhand.domain.model.BuyerProduct
import com.and2t2.secondhand.domain.model.BuyerProductMapper

class HomeRepo(
    private val sellerService: SellerService,
    private val buyerService: BuyerService,
    private val buyerMapper : BuyerProductMapper,
    private val mDb : DatabaseSecondHand
) {
    suspend fun getKategori() = sellerService.getSellerCategory()

    suspend fun getBuyerMapper(status : String?,categoryId : Int?,search : String?) : List<BuyerProduct>{
        val result =  buyerService.getBuyerProduct(status, categoryId, search)
        return buyerMapper.toDomainList(result)
    }
    val buyerDao = mDb.buyerDao()
    fun getBuyerProduct(status : String?,categoryId : Int?,search : String?) = networkBoundResource(
        query = {
            buyerDao.getProduct()
        }  ,
        fetch = {
            getBuyerMapper(status, categoryId, search)
        },
        saveFetchResult = { buyerProduct ->
            mDb.withTransaction {
                buyerDao.deleteProduct()
                buyerDao.insertProduct(buyerProduct)
            }
        }
    )


}