package com.and2t2.secondhand.domain.repository

import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.data.remote.SellerService

class HomeRepo(
    private val sellerService: SellerService
) {
    suspend fun getKategori() = sellerService.getSellerCategory()
}