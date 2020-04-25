package com.journal.utils

import android.text.Html


fun String?.stripHtml(): String {
    return if (isNullOrEmpty()) {
        ""
    } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString().replace("\n", "").trim()
    } else {
        Html.fromHtml(this).toString().replace("\n", "").trim()
    }
}