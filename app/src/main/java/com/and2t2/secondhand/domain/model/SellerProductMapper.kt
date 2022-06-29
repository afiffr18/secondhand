package com.and2t2.secondhand.domain.model

import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.data.remote.dto.seller.SellerProductDtoItem

class SellerProductMapper: DomainMapper<SellerProductDtoItem, SellerProduct> {
    override fun mapToDomainModel(modelDto: SellerProductDtoItem): SellerProduct {
        return SellerProduct(
            id = modelDto.id,
            userId = modelDto.userId,
            productName = modelDto.name,
            description = modelDto.description,
            basePrice = modelDto.basePrice,
            imageUrl = modelDto.imageUrl,
            location = modelDto.location,
            status = modelDto.status,
            categories = modelDto.categories.joinToString { it.name }
        )
    }

    fun toDomainList(initial: List<SellerProductDtoItem>): List<SellerProduct> {
        return initial.map {
            mapToDomainModel(it)
        }
    }
}