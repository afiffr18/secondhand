package com.and2t2.secondhand.common

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun Int.toRp() : String{
    val locale = Locale.getDefault()

    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    numberFormat.maximumFractionDigits = 0
    val convert = numberFormat.format(this)

    return convert
}

fun String.toFormatDate() : String{

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMMM,HH:mm", Locale.getDefault())


    val inputDate = inputFormat.parse(this)
    inputDate.let {
        return outputFormat.format(it!!)
    }
}