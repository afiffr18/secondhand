package com.and2t2.secondhand.common

import android.content.Context
import android.os.Handler
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


fun Int.toRp() : String{
    val locale = Locale("in","ID")
    val numberFormat = NumberFormat.getCurrencyInstance(locale)
    numberFormat.maximumFractionDigits = 0
    return  numberFormat.format(this)

}

fun String.toFormatPhone() : String{

    val result = if(this.subSequence(0,2) == "08"){
        val tes = this.subSequence(0,2)
        this.replace(tes.toString(),"628")
    }else{
      this.replace("+","")
    }


    return result
}


fun String.toFormatDate() : String {

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMMM,HH:mm", Locale.getDefault())


    val inputDate = inputFormat.parse(this)
    inputDate.let {
        return outputFormat.format(it!!)
    }
}

//For fragment
fun Fragment.hideKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
}

////For Activity
//fun AppCompatActivity.hideKeyboard() {
//    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//    imm.hideSoftInputFromWindow(this.window.attributes.token, 0)
//}

//search on keyboard done
fun EditText.onDone(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            callback.invoke()
        }
            false

    }
}

fun ViewPager2.autoScroll(interval: Long,) {

    val handler = Handler()
    var scrollPosition = 0

    val runnable = object : Runnable {

        override fun run() {

            /**
             * Calculate "scroll position" with
             * adapter pages count and current
             * value of scrollPosition.
             */
            setCurrentItem(scrollPosition++ % adapter?.itemCount!!, true)

            handler.postDelayed(this, interval)
        }
    }

    registerOnPageChangeCallback(object: OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            // Updating "scroll position" when user scrolls manually
            scrollPosition = position + 1
        }

        override fun onPageScrollStateChanged(state: Int) {
            // Not necessary
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            // Not necessary
        }
    })

    handler.post(runnable)
}
