package com.sachin.app.smsdetection.data.filter

import com.sachin.app.smsdetection.data.model.LocalSms
import com.sachin.app.smsdetection.data.parser.BankSmsParser
import org.junit.Test
import java.util.*

class SmsParserTest {

    @Test
    fun checkHdfcSmsParser() {
        val bankSms = "Your a/c ending XXXXXXXX452 is debited by Rs. 3000.00 on 02-Mar-22 and a/c ending XXXXXXXXX650 credited (IMPS Ref no 206116080622). Team IDFC FIRST Bank"
        val cardSms = "Transaction Alert: INR 3,869.00 has been spent on your YES BANK Credit Card ending with 0823 at EASY TRIP PLANNERS on 06-08-2022 at 06:11:28 pm. Avl Bal INR 72,829.83. In case of suspicious transaction, to block your card, SMS BLKCC {Space}{Last 4 digits of card number} to 9840909000 from your registered mobile number. 19:18 06 Aug 22"
            //"You've spent Rs.1530 On HDFC Bank CREDIT Card xx0362 At AMAZON On 2022-09-05:14:36:32 Avl bal: Rs.283981 Curr O/s: Rs.42019 Not you?Call 18002586161"

        val message = BankSmsParser.parseSms(
            LocalSms("VK-HDFC", bankSms, Date().time)
        )
        println(message)
        assert(true)
    }
}