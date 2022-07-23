package com.and2t2.secondhand.domain.model

import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.common.toFormatDate
import com.and2t2.secondhand.data.remote.dto.seller.SellerOrderDtoItem
import com.and2t2.secondhand.data.remote.dto.seller.SellerProductDtoItem

class SellerOrderMapper: DomainMapper<SellerOrderDtoItem, SellerOrder> {
    override fun mapToDomainModel(modelDto: SellerOrderDtoItem): SellerOrder {
        return SellerOrder(
            id = modelDto.id,
            productId = modelDto.productId,
            buyerId = modelDto.buyerId,
            price = modelDto.price,
            productName = modelDto.productName,
            basePrice = modelDto.product.basePrice,
            imageProduct = modelDto.imageProduct,
            status = modelDto.status,
            updatedAt = modelDto.updatedAt.toFormatDate(),
            createdAt = modelDto.createdAt,
            Product = modelDto.product.name,
            phoneNumber = modelDto.user.phoneNumber,
            buyerName = modelDto.user.fullName,
            buyerLocation = modelDto.user.city,
            productStatus = modelDto.product.status
        )
    }

    fun toDomainList(initial: List<SellerOrderDtoItem>): List<SellerOrder> {
        return initial.map {
            mapToDomainModel(it)
        }
    }
}