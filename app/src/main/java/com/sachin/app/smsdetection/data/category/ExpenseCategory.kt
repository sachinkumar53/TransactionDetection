package com.sachin.app.smsdetection.data.category

enum class ExpenseCategory(
    val keywords: List<String>
) {
    Food(
        listOf(
            "stall",
            "food",
            "egg",
            "chicken",
            "swiggy",
            "zomato",
            "domino",
            "starbuck",
            "kfc",
            "mojo",
            "eat",
            "pizza",
            "burger",
            "coffee",
        )
    ),
    Grocery(
        listOf(
            "vegetable",
            "grocery",
            "fruit",
            "milk",
            "fresh",
            "country delight",
            "big basket",
            "grofers",
            "blink it",
            "zepto",
            "fraazo"
        )
    ),
    Shopping(
        listOf(
            "shop",
            "market",
            "store",
            "mall",
            "mart",
            "amazon",
            "flipkart",
            "myntra",
            "reliance digital",
            "jio mart"
        )
    ),
    Taxis(
        listOf(
            "ride",
            "taxi",
            "cab",
            "ola",
            "uber",
            "rapido",
        )
    ),
    Travel(
        listOf(
            "flight",
            "bus",
            "train",
            "trip",
            "irctc",
            "mmt",
            "make my trip",
            "goibibo",
            "agoda",
            "hotel",
            "ixigo",
            "trivago"
        )
    ),
    Entertainment(
        listOf(
            "movie",
            "video",
            "book my show",
            "netflix",
            "prime video",
            "hotstar",
            "voot",
            "zee5",
            "youtube"
        )
    ),
    Payment(
        listOf(
            "upi",
            //"paytm",
            "bank",
            "pay",
            "bharatpe",
            "phonepe",
            "neft",
            "rtgs",
            "imps",
            "cheque",
            "demand draft"
        )
    ),
    Other(emptyList());

    companion object {

        fun findCategoryByText(text: String): ExpenseCategory {
            for (category in values()) {
                if (category.keywords.any { text.contains(it, true) })
                    return category
            }
            return Other
        }


        fun getCategoryByKeyword(keyword: String?): ExpenseCategory {
            val key = keyword?.uppercase() ?: return Other
            return when {
                key.contains("SWIGGY|ZOMATO|DOMINO|DOMINOS|STARBUCK".toRegex()) -> Food
                key.contains("AMAZON|FLIPKART|MYNTRA|RELIANCE DIGITAL".toRegex()) -> Shopping
                key.contains("BIG BASKET|GROFERS|ZEPTO|FRAAZO|FRESH TO HOME".toRegex()) -> Grocery
                key.contains("OLA|UBER|RAPIDO".toRegex()) -> Taxis
                key.contains("BOOK MY SHOW|NETFLIX|PRIME VIDEO|HOTSTAR|VOOT|ZEE5".toRegex()) -> Entertainment
                key.contains("PAYTM|PAY|BHARATPE|PHONEPE".toRegex()) -> Payment
                key.contains("TRIP|GOIBIBO|AGODA|IXIGO|IRCTC|TRIVAGO".toRegex()) -> Travel
                else -> Other
            }
        }
    }
}