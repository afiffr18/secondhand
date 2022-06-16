package com.and2t2.secondhand

import com.and2t2.secondhand.common.toFormatDate
import org.junit.Assert
import org.junit.Test

class ExtFunctionTest {

    @Test
    fun formatDate(){
        val date = "2022-06-14T07:50:47.226Z"
        Assert.assertEquals("14 June,07:50", date.toFormatDate())
    }

}