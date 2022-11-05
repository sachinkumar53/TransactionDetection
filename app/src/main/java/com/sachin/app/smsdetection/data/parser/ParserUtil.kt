package com.sachin.app.smsdetection.data.parser

object ParserUtil {

    fun getMerchantName(text: String): String? {
        val pattern1 = "(\\b[Tt]o\\b)([\\w\\s]+)(\\b[Oo]n\\b)".toRegex()
        val pattern2 = "(\\b[Ff]rom\\b)([\\w\\s]+)(\\b[Ii]n\\b)".toRegex()
        var name: String? = null

        if (text.contains(pattern1)) {
            val matcher = pattern1.toPattern().matcher(text)
            if (matcher.find())
                name = matcher.group(2)?.trim()
        } else if (text.contains(pattern2)) {
            val matcher = pattern2.toPattern().matcher(text)
            if (matcher.find())
                name = matcher.group(2)?.trim()
        }
        name = removeSpecialChar(name)
        return name
    }

    fun removeSpecialChar(text: String?): String? {
        if (!text.isNullOrEmpty()) {
            val symbols = "^[0-9+,.]|[0-9+,.]$".toRegex()
            if (text.contains(symbols))
                return text.replace(symbols, "").smartCapitalize()
        }
        return text
    }

    fun getMerchantUpiId(text: String): String? {
        val matcher = "(to\\s)(VPA\\s)?([\\w.-]+@[\\w.-]+)".toPattern().matcher(text)
        return if (matcher.find()) {
            matcher.group(3)
        } else null
    }

    fun getRefNo(text: String): String? {
        val refNoPattern = "\\d{12}".toRegex()
        val refMatcher = refNoPattern.toPattern().matcher(text)
        return if (refMatcher.find())
            refMatcher.group(0)
        else null
    }

    /*fun getTransactionType(text: String): String {

        return when {
            text.lowercase()
                .contains("debited|spent|sent|deducted|paid|withdrawn".toRegex()) -> "debited"
            text.lowercase().contains("credited|refund|refunded|deposited".toRegex()) -> "credited"
            else -> "Unknown"
        }
    }*/

    fun getFormattedAmount(amount: String): String {
        return amount.replace("(([Rr]s\\.?)|INR)\\s*".toRegex(), "₹ ")
    }
}

fun String.smartCapitalize(): String {
    val parts = lowercase().split(" ")
    return parts.joinToString(separator = " ") { string ->
        string.replaceFirstChar { it.uppercaseChar() }
    }
}