package com.sachin.app.smsdetection.data.merchant

import com.sachin.app.smsdetection.data.parser.SmsParser

interface Merchant {
    val parser: SmsParser
}