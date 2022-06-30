package com.and2t2.secondhand.domain.repository

import androidx.room.withTransaction
import com.and2t2.secondhand.common.networkBoundResource
import com.and2t2.secondhand.data.local.DatabaseSecondHand
import com.and2t2.secondhand.data.remote.BuyerService
import com.and2t2.secondhand.data.remote.dto.buyer.PostBuyerOrderBody
import com.and2t2.secondhand.data.remote.dto.buyer.PostBuyerOrderDto
import com.and2t2.secondhand.domain.model.BuyerProduct
import com.and2t2.secondhand.domain.model.BuyerProductMapper
import retrofit2.Response

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

//    suspend fun setBuyerOrder(access_token : String,postBuyerOrderBody: PostBuyerOrderBody) = apiService.setBuyerOrder(access_token,postBuyerOrderBody)

    suspend fun setBuyerOrder(postBuyerOrderBody: PostBuyerOrderBody) : Response<PostBuyerOrderDto> {
       return apiService.postBuyerOrder(postBuyerOrderBody,"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImFmaWZmdWppYXJhaG1hbjIwMDBAZ21haWwuY29tIiwiaWF0IjoxNjU1OTk3MzY5fQ.BW4cO5Q5A9TFSf8k6viT8wFCYcueQVm7x45W6FnoSbw")
    }




}

