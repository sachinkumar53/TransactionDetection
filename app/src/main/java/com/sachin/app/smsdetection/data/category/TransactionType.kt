package com.sachin.app.smsdetection.data.category

enum class TransactionType(
    val keywords: List<String>
) {
    Credit(
        listOf(
            "credited",
            "received",
            "deposited",
            "refunded"
        )
    ),
    Debit(
        listOf(
            "debited",
            "paid",
            "deducted",
            "sent",
            "spent",
            "withdrawn"
        )
    ),

    //Refund,
    Unknown(emptyList());

    companion object {
        fun findTransactionTypeByText(text: String): TransactionType {
            for (type in values()) {
                if (type.keywords.any { text.contains(it, true) })
                    return type
            }
            return Unknown
        }
    }

}