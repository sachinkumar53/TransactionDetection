package com.sachin.app.smsdetection.data.filter

import com.sachin.app.smsdetection.data.parser.ParserUtil
import org.junit.Test

class ParserUtilTest {

    @Test
    fun checkFormattedAmount() {
        val amount = "Rs 1500.00"
        val expectedAmount = "₹ 1500.00"
        val formattedAmount = ParserUtil.getFormattedAmount(amount)
        assert(formattedAmount == expectedAmount)
    }
}