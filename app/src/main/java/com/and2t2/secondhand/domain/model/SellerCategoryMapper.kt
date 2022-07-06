package com.and2t2.secondhand.domain.model

import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.data.remote.dto.seller.SellerCategoryDtoItem

class SellerCategoryMapper: DomainMapper<SellerCategoryDtoItem, SellerCategory> {
    override fun mapToDomainModel(modelDto: SellerCategoryDtoItem): SellerCategory {
        return SellerCategory(
            id = modelDto.id,
            name = modelDto.name
        )
    }

    fun toDomainList(initial: List<SellerCategoryDtoItem>): List<SellerCategory> {
        return initial.map {
            mapToDomainModel(it)
        }
    }
}