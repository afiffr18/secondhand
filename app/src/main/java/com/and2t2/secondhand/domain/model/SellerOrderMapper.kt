package com.and2t2.secondhand.domain.model

import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.data.remote.dto.seller.SellerOrderDtoItem

class SellerOrderMapper: DomainMapper<SellerOrderDtoItem, SellerOrder> {
    override fun mapToDomainModel(modelDto: SellerOrderDtoItem): SellerOrder {
        return SellerOrder(
            id = modelDto.id,
            productId = modelDto.productId,
            buyerId = modelDto.buyerId,
            price = modelDto.price,
            productName = modelDto.product.name,
            basePrice = modelDto.product.basePrice,
            imageProduct = modelDto.product.imageUrl,
            status = modelDto.status,
            updatedAt = modelDto.updatedAt
        )
    }
}