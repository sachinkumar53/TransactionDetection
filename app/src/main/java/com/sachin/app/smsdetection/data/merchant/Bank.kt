package com.sachin.app.smsdetection.data.merchant

import com.sachin.app.smsdetection.data.parser.BankSmsParser
import com.sachin.app.smsdetection.data.parser.SmsParser

enum class Bank : Merchant {
    CENTBK,
    HDFCBK,
    ICICIB,
    SBIINB,
    SBMSMS,
    SCISMS,
    CBSSBI,
    SBIPSG,
    SBIUPI,
    SBICRD,
    ATMSBI,
    QPMYAME,
    IDFCFB,
    UCOBNK,
    CANBNK,
    BOIIND,
    AXISBK,
    PAYTMB,
    UnionB,
    INDBNK,
    KOTAKB,
    SCBANK,
    PNBSMS,
    DOPBNK,
    YESBNK,
    IDBIBK,
    ALBANK,
    CITIBK,
    ANDBNK,
    BOBTXN,
    IOBCHN,
    MAHABK,
    OBCBNK,
    RBLBNK,
    RBLCRD,
    SPRCRD,
    HSBCBK,
    HSBCIN,
    INDUSB;

    override val parser: SmsParser
        get() = BankSmsParser
}