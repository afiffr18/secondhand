package com.and2t2.secondhand.common

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.io.ByteArrayOutputStream

fun Int.toRp() : String{
    val locale = Locale.getDefault()

    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    numberFormat.maximumFractionDigits = 0
    val convert = numberFormat.format(this)

    return convert
}

fun String.toFormatDate() : String {

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMMM,HH:mm", Locale.getDefault())


    val inputDate = inputFormat.parse(this)
    inputDate.let {
        return outputFormat.format(it!!)
    }
}


inline fun <reified T : ViewModel> ComponentActivity.viewModelsFactory(crossinline viewModelInitialization: () -> T): Lazy<T> {
    return viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewModelInitialization.invoke() as T
            }
        }
    }
}

inline fun <reified T : ViewModel> Fragment.viewModelsFactory(crossinline viewModelInitialization: () -> T): Lazy<T> {
    return viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return viewModelInitialization.invoke() as T
            }
        }
    }
}

// Random String Generator
fun getRandomString() : String {
    val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return List(8) { charset.random() }
        .joinToString("")
}

// Convert Bitmap to Uri
fun bitmapToUri(inContext: Context, inImage: Bitmap): Uri {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(
        inContext.contentResolver,
        inImage,
        "secondhand_${getRandomString()}",
        null
    )
    Log.d("image uri", path)
    return Uri.parse(path)
}