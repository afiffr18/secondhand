package com.and2t2.secondhand.domain.repository

import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.data.remote.BuyerService
import com.and2t2.secondhand.domain.model.BuyerProduct
import com.and2t2.secondhand.domain.model.BuyerProductMapper

class BuyerRepo(
    private val apiService: BuyerService,
    private val mapper: BuyerProductMapper
) {

    suspend fun getBuyerProductById(id : Int) : BuyerProduct{
        val result = apiService.getBuyerProductById(id)
        return mapper.mapToDomainModel(result)
    }
}