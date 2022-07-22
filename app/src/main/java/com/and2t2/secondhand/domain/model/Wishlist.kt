package com.and2t2.secondhand.domain.model

data class Wishlist(
    val id : Int,
    val productId : Int? = null,
    val product_image : String? = null,
    val product_name : String? = null,
    val base_price : Int ? = null,
    val categories : String? = null
)