package com.and2t2.secondhand.domain.model

import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.data.remote.dto.seller.SellerOrderDtoItem

class SellerOrderMapper : DomainMapper<SellerOrderDtoItem,SellerOrder> {
    override fun mapToDomainModel(modelDto: SellerOrderDtoItem): SellerOrder {
        return SellerOrder(
            id = modelDto.id,
            namaBarang = modelDto.productName,
            price = modelDto.price,
            basePrice = modelDto.basePrice,
            imageProduct = modelDto.product.imageUrl,
            status = modelDto.status,
            date = modelDto.createdAt
        )
    }

    fun toDomainList(initial : List<SellerOrderDtoItem>) : List<SellerOrder>{
        return initial.map{
            mapToDomainModel(it)
        }
    }
}