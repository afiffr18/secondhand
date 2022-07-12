package com.and2t2.secondhand

import android.telephony.PhoneNumberUtils
import com.and2t2.secondhand.common.toFormatDate
import com.and2t2.secondhand.common.toFormatPhone
import org.junit.Assert
import org.junit.Test

class ExtFunctionTest {

    @Test
    fun formatDate(){
        val date = "2022-06-14T07:50:47.226Z"
        Assert.assertEquals("14 June,07:50", date.toFormatDate())
    }

    @Test
    fun toNumberFormat(){
        val number = "+6281261738399"

//        val result = if(number.subSequence(0,2) == "08"){
//            val tes = number.subSequence(0,2)
//            number.replace(tes.toString(),"628")
//        }else{
//            number.replace("+","")
//        }
        Assert.assertEquals("6281261738399",number.toFormatPhone())
}

}