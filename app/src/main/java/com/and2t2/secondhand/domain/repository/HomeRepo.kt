package com.and2t2.secondhand.domain.repository

import androidx.room.withTransaction
import com.and2t2.secondhand.common.networkBoundResource
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.BuyerService
import com.and2t2.secondhand.data.remote.SellerService
import com.and2t2.secondhand.data.remote.dto.seller.SellerBannerDtoX
import com.and2t2.secondhand.domain.model.*

class HomeRepo(
    private val sellerService: SellerService,
    private val buyerService: BuyerService,
    private val buyerMapper : BuyerProductMapper,
    private val sellerCategoryHomeMapper: SellerCategoryHomeMapper,
    private val mDb : DatabaseSecondHand
) {
    suspend fun getKategori() : List<SellerCategory>{
        val result = sellerService.getSellerCategory()
        return sellerCategoryHomeMapper.toDomainList(result)
    }
    private val sellerCategoryDao = mDb.sellerDao()
    fun getSellerKategori() = networkBoundResource(
        query = {
            sellerCategoryDao.getSellerCategory()
        },
        fetch = {
            getKategori()
        },
        saveFetchResult = {
            mDb.withTransaction {
                sellerCategoryDao.deleteSellerCategory()
                sellerCategoryDao.insertSellerCategory(it)
            }
        }
    )

    suspend fun getBuyerMapper(status : String?,categoryId : Int?,search : String?) : List<BuyerProduct>{
        val result =  buyerService.getBuyerProduct(status, categoryId, search)
        return buyerMapper.toDomainList(result)
    }
    private val buyerDao = mDb.buyerDao()
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

    suspend fun getBanner() : SellerBannerDtoX {
        return sellerService.getSellerBanner()
    }


}