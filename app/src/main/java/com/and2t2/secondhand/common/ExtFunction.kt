package com.and2t2.secondhand.common

import java.text.NumberFormat
import java.util.*

fun Int.toRp() : String{
    val locale = Locale.getDefault()

    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    numberFormat.maximumFractionDigits = 0
    val convert = numberFormat.format(this)

    return convert
}