package com.sachin.app.smsdetection.data.model

import com.sachin.app.smsdetection.data.category.ExpenseCategory
import com.sachin.app.smsdetection.data.category.TransactionType

sealed interface TransactionDetail {
    val title: String?
    val category: ExpenseCategory
    val amount: String?
    val date: String?
    val upiRefNumber: String?
    val transactionType: TransactionType
    val sms: LocalSms

    data class Bank(
        override val title: String?,
        override val category: ExpenseCategory,
        override val amount: String?,
        override val date: String?,
        override val upiRefNumber: String?,
        override val transactionType: TransactionType,
        val accountNumber: String?,
        override val sms: LocalSms
    ) : TransactionDetail

    data class Card(
        override val title: String?,
        override val category: ExpenseCategory,
        override val amount: String?,
        override val date: String?,
        override val upiRefNumber: String?,
        override val transactionType: TransactionType,
        val cardNumber: String?,
        override val sms: LocalSms
    ) : TransactionDetail
}
