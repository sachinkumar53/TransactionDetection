package com.sachin.app.smsdetection.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalSms(
    val sender: String,
    val text: String,
    val date: Long?
) : Parcelable