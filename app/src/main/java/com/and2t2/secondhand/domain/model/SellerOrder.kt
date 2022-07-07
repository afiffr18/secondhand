package com.and2t2.secondhand.domain.model

data class SellerOrder(
    val id : Int?=null,
    val namaBarang : String?=null,
    val price : Int?=null,
    val basePrice : String?=null,
    val imageProduct : String?=null,
    val status : String?=null,
    val date : String?=null
)
