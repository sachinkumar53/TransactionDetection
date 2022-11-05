package com.sachin.app.smsdetection.data

import android.content.Context
import android.provider.Telephony
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.sachin.app.smsdetection.data.filter.SmsClassifier
import com.sachin.app.smsdetection.data.model.LocalSms
import com.sachin.app.smsdetection.data.model.TransactionDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SMSLoader(
    private val context: Context
) {

    suspend fun loadSMS() = withContext(Dispatchers.IO) {
        val contentResolver = context.contentResolver
        contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            arrayOf(
                Telephony.Sms.Inbox.BODY,
                Telephony.Sms.Inbox.ADDRESS,
                Telephony.Sms.Inbox.DATE
            ),
            null,
            null,
            Telephony.Sms.Inbox.DATE + " DESC"
        )?.use {
            val list = mutableListOf<TransactionDetail>()
            while (it.moveToNext()) {
                val body = it.getStringOrNull(0)
                val address = it.getStringOrNull(1)
                val date = it.getLongOrNull(2)

                if (!address.isNullOrEmpty() && !body.isNullOrEmpty()) {
                    val sms = LocalSms(
                        text = body,
                        sender = address,
                        date = date
                    )
                    SmsClassifier.classify(sms)?.let { merchant ->
                        list.add(merchant.parser.parseSms(sms))
                    }
                }
            }
            list
        }
    }
}


private const val TAG = "SMSLoader"

