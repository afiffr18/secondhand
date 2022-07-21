package com.and2t2.secondhand.domain.model

import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.data.remote.dto.seller.SellerCategoryDtoItem

class SellerCategoryHomeMapper : DomainMapper<SellerCategoryDtoItem,SellerCategory> {
    private val listOfCategory: MutableList<SellerCategory> = mutableListOf()
    override fun mapToDomainModel(modelDto: SellerCategoryDtoItem): SellerCategory {
        return  SellerCategory(
            id = modelDto.id,
            name = modelDto.name
        )
    }

    fun toDomainList(initial : List<SellerCategoryDtoItem>) : List<SellerCategory>{
        val newKategori = SellerCategory(0,"Semua")
        listOfCategory.add(newKategori)
        val data = initial.map {
            mapToDomainModel(it)
        }
        data.map { kategori ->
            listOfCategory.add(kategori)
        }
        return listOfCategory
    }
}