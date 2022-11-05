package com.sachin.app.smsdetection.data.parser

import com.sachin.app.smsdetection.data.category.ExpenseCategory
import com.sachin.app.smsdetection.data.category.TransactionType
import com.sachin.app.smsdetection.data.model.LocalSms
import com.sachin.app.smsdetection.data.model.TransactionDetail
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object BankSmsParser : SmsParser {
    /**
     * Sample transaction
     * HDFC Bank: Rs 31133.00 debited from a/c **9612 on 07-09-22 to
     * VPA cred.club@axisb(UPI Ref No 225016920384). Not you? Call on 18002586161 to report
     */

    /**
     * Sample Credit card transaction
     * You've spent Rs.1530 On HDFC Bank CREDIT Card xx0362 At AMAZON On 2022-09-05:14:36:32
     * Avl bal: Rs.283981 Curr O/s: Rs.42019 Not you?Call 18002586161
     */

    private const val AMOUNT_PATTERN = "([Rr]s\\.?|INR)\\s*([\\d,]+\\.?\\d*)"

    override fun parseSms(sms: LocalSms): TransactionDetail {
        val text = sms.text
        return if (
            text.contains("Credit card", true)
            || text.contains("Debit card", true)
        ) {
            parseCardSms(sms)
        } else {
            parseBankSms(sms)
        }
    }


    private fun parseCardSms(sms: LocalSms): TransactionDetail {
        val spentOn = getSpentOn(sms.text)
        return TransactionDetail.Card(
            title = spentOn?.smartCapitalize(),
            category = ExpenseCategory.getCategoryByKeyword(spentOn),
            amount = getAmount(sms.text),
            date = getDate(sms),
            cardNumber = getCardNumber(sms.text),
            upiRefNumber = ParserUtil.getRefNo(sms.text),
            transactionType = TransactionType.findTransactionTypeByText(sms.text),
            sms = sms
        )
    }

    private fun parseBankSms(sms: LocalSms): TransactionDetail {
        var merchant = ParserUtil.getMerchantName(sms.text)?.smartCapitalize()
        if (merchant.isNullOrEmpty())
            merchant = ParserUtil.getMerchantUpiId(sms.text) ?: "Merchant"

        return TransactionDetail.Bank(
            title = merchant,
            category = ExpenseCategory.findCategoryByText(sms.text),
            amount = getAmount(sms.text),
            date = getDate(sms),
            upiRefNumber = ParserUtil.getRefNo(sms.text),
            transactionType = TransactionType.findTransactionTypeByText(sms.text),
            accountNumber = getAccountNumber(sms.text),
            sms = sms
        )

    }

    private fun getAccountNumber(text: String): String? {
        var accNumber: String? = null
        val accNoPattern1 =
            "([Aa]/[Cc])((\\s?[Nn]o[.:]?)|(\\s?ending)|(\\s?ending\\swith))?\\s*([0-9]*[Xx*.]+\\d{3,6})".toRegex()
        val accNoPattern2 = "([0-9Xx]+\\d{4})".toRegex()

        if (text.contains(accNoPattern1)) {
            val matcher = accNoPattern1.toPattern().matcher(text)
            if (matcher.find()) {
                accNumber = matcher.group(6)
            }
        } else if (text.contains(accNoPattern2)) {
            val matcher = accNoPattern2.toPattern().matcher(text)
            if (matcher.find()) {
                accNumber = matcher.group()
            }
        }

        return accNumber
    }

    private fun getCardNumber(text: String): String? {
        var cardNumber: String? = null
        val cardPattern1 = "([Cc]ard)\\s*([Xx]*\\d{4})".toRegex()
        val cardPattern2 = "([Cc]ard\\sending)(\\swith)?\\s*([Xx]*\\d{4})".toRegex()

        if (text.contains(cardPattern1)) {
            val matcher = cardPattern1.toPattern().matcher(text)
            if (matcher.find())
                cardNumber = matcher.group(2)
        } else if (text.contains(cardPattern2)) {
            val matcher = cardPattern2.toPattern().matcher(text)
            if (matcher.find())
                cardNumber = matcher.group(3)
        }
        return cardNumber
    }

    private fun getSpentOn(text: String): String? {
        val pattern = "(\\b[Aa]t\\b)([\\w\\s&,.+]+)(\\b[Oo]n\\b)".toRegex()
        var spentOn: String? = null
        if (text.contains(pattern)) {
            val matcher = pattern.toPattern().matcher(text)
            if (matcher.find()) {
                spentOn = matcher.group(2)?.trim()
            }
        }

        spentOn = ParserUtil.removeSpecialChar(spentOn)
        return spentOn
    }


    private fun getDate(sms: LocalSms): String? {
        return SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(sms.date)
    }

    private fun getAmount(body: String): String? {
        val amountMatcher = Pattern.compile(AMOUNT_PATTERN).matcher(body)
        val amount = if (amountMatcher.find()) {
            amountMatcher.group(2)?.let { "₹ $it" }
            //amountMatcher.group(0)?.let { ParserUtil.getFormattedAmount(it) }
        } else null
        return amount
    }
}