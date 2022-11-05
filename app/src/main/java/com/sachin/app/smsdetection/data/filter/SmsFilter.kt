package com.sachin.app.smsdetection.data.filter

import com.sachin.app.smsdetection.data.merchant.Merchant
import com.sachin.app.smsdetection.data.model.LocalSms

abstract class SmsFilter {

    abstract fun recognizeSms(sms: LocalSms): Merchant?

}