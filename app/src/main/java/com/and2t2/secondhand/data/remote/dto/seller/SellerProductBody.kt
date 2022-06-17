package com.and2t2.secondhand.data.remote.dto.seller

data class SellerProductBody(
    val name : String? = null,
    val description : String? = null,
    val base_price : Int? = null,
    val category_ids : List<Category> = emptyList(),
    val location : String? = null,
    val image : String? = null
)
