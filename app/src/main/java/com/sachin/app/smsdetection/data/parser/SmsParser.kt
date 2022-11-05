package com.sachin.app.smsdetection.data.parser

import com.sachin.app.smsdetection.data.model.LocalSms
import com.sachin.app.smsdetection.data.model.TransactionDetail

interface SmsParser {
    fun parseSms(sms: LocalSms): TransactionDetail
}