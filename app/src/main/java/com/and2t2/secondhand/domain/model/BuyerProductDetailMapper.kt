package com.and2t2.secondhand.domain.model

import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.data.remote.dto.buyer.BuyerProductDtoItem

class BuyerProductDetailMapper : DomainMapper<BuyerProductDtoItem,BuyerProductDetail> {
    override fun mapToDomainModel(modelDto: BuyerProductDtoItem): BuyerProductDetail {
        return BuyerProductDetail(
            id = modelDto.id,
            namaBarang = modelDto.name,
            deskripsiBarang = modelDto.description,
            hargaBarang = modelDto.basePrice,
            imageUrl = modelDto.imageUrl,
            lokasi = modelDto.location,
            kategori = if(modelDto.categories.isNullOrEmpty()){
                "Kategori"
            }else{
                modelDto.categories[0].name
            }
        )
    }

    fun toDomainList(initial : List<BuyerProductDtoItem>) : List<BuyerProductDetail>{
        return initial.map{
            mapToDomainModel(it)
        }
    }

}