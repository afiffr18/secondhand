package com.and2t2.secondhand.domain.repository

import com.and2t2.secondhand.data.remote.SellerService

class SellerRepo(private val sellerService: SellerService) {
    suspend fun getProduct(accessToken: String) = sellerService.getSellerProduct(accessToken)
}