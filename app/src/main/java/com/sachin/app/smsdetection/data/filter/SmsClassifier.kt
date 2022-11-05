package com.sachin.app.smsdetection.data.filter

import com.sachin.app.smsdetection.data.filter.bank.BankSmsFilter
import com.sachin.app.smsdetection.data.merchant.Merchant
import com.sachin.app.smsdetection.data.model.LocalSms

object SmsClassifier {
    private val filters = listOf<SmsFilter>(BankSmsFilter)

    fun classify(sms: LocalSms): Merchant? {
        for (filter in filters) {
            return filter.recognizeSms(sms)
        }
        return null
    }

    /*fun registerFilter(filter: SmsFilter) {
        filters.add(filter)
    }

    fun unregisterFilter(filter: SmsFilter) {
        filters.remove(filter)
    }*/

}