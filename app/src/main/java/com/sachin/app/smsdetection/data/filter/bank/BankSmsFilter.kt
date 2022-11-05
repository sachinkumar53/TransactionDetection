package com.sachin.app.smsdetection.data.filter.bank

import com.sachin.app.smsdetection.data.filter.SmsFilter
import com.sachin.app.smsdetection.data.merchant.Bank
import com.sachin.app.smsdetection.data.merchant.Merchant
import com.sachin.app.smsdetection.data.model.LocalSms
import com.sachin.app.smsdetection.data.util.FilterUtil

private const val TAG = "BankSmsFilter"

object BankSmsFilter : SmsFilter() {

    override fun recognizeSms(sms: LocalSms): Merchant? {
        val (sender, text, _) = sms

        if (!isTransactionSms(text)) return null

        val bank: Bank = if (sender.contains('-')) {
            if (FilterUtil.isSmsFormatValid(sender))
                Bank.values().find { sender.endsWith(it.name, true) }
            else null
        } else {
            Bank.values().find {
                sender.substring(2, sender.length).equals(it.name, true)
            }
        } ?: return null

        return bank
    }

    private fun isTransactionSms(text: String): Boolean {
        val body = text.lowercase()
        val accNoPattern1 =
            "([Aa]/[Cc])((\\s?[Nn]o[.:]?)|(\\s?ending)|(\\s?ending\\swith))?\\s*([0-9]*[Xx*.]+\\d{3,6})".toRegex()
        val accNoPattern2 = "([0-9Xx]+\\d{4})".toRegex()

        val cardPattern1 = "([Cc]ard)\\s*([Xx]*\\d{4})".toRegex()
        val cardPattern2 = "([Cc]ard\\sending)(\\swith)?\\s*([Xx]*\\d{4})".toRegex()

        if (body.contains("AutoPay", true) || body.contains("recurring", true))
            return false

        if (listOf(
                "credited",
                "received",
                "deposited",
                "refunded",
                "debited",
                "deducted",
                "sent",
                "spent",
                "paid",
                "withdrawn"
            ).any { body.contains(it, true) }
        ) {
            if (body.contains("([Rr]s\\.?|INR)\\s*".toRegex()))
                if (body.contains("a/c", true) || body.contains("card", true))
                    if (body.contains(accNoPattern2)
                        || body.contains(accNoPattern1)
                        || body.contains(cardPattern1)
                        || body.contains(cardPattern2)
                    )
                        return true
        }
        return false
    }

}