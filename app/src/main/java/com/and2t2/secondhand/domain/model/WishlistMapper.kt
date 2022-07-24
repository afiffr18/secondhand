package com.and2t2.secondhand.domain.model

import com.and2t2.secondhand.common.DomainMapper
import com.and2t2.secondhand.data.remote.dto.wishlist.WishlistDtoItem

class WishlistMapper : DomainMapper<WishlistDtoItem,Wishlist> {
    override fun mapToDomainModel(modelDto: WishlistDtoItem): Wishlist {
        return Wishlist(
            id = modelDto.id,
            productId = modelDto.productId,
            product_image = modelDto.product.imageUrl,
            product_name = modelDto.product.name,
            base_price = modelDto.product.basePrice,
            categories = if(modelDto.product.categories.isNullOrEmpty()){
                "Kategori"
            }else{
                modelDto.product.categories[0].name
            }
        )
    }

    fun toDomainList(initial : List<WishlistDtoItem>) : List<Wishlist>{
        val dataWishlist =  initial.filter {
            it.product != null
        }
        return dataWishlist.map {
            mapToDomainModel(it)
        }
    }
}