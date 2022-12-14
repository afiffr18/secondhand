package com.and2t2.secondhand.domain.model

import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.data.remote.dto.buyer.BuyerProductDtoItem

class BuyerProductMapper : DomainMapper<BuyerProductDtoItem,BuyerProduct> {
    override fun mapToDomainModel(modelDto: BuyerProductDtoItem): BuyerProduct {
        return BuyerProduct(
            id = modelDto.id,
            namaBarang = modelDto.name,
            imageUrl = modelDto.imageUrl,
            kategori = if(modelDto.categories.isNullOrEmpty()){
                "Kategori"
            }else{
                modelDto.categories[0].name
            },
            hargaBarang = modelDto.basePrice

        )
    }

    fun toDomainList(initial : List<BuyerProductDtoItem>) : List<BuyerProduct> {
        return initial.map {
            mapToDomainModel(it)
        }
    }
}