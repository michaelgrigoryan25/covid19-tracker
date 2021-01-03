package com.michaelgrigoryan.covidtracker.util

import android.text.format.DateUtils
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/*
 * Created by Mbuodile Obiosio on Jan 03, 2021
 * Twitter @cazewonder
 * Nigeria.
 */

fun formatNumber(number: Int?): String {
    val numberFormat = DecimalFormat("#,###,###")
    return numberFormat.format(number)
}

fun getLastUpdate(date: String?): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())
    val format = sdf.parse(date!!)
    return DateUtils.getRelativeTimeSpanString(format?.time ?: 0).toString()
}

fun formatDate(date: String): String{
    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val formatter = SimpleDateFormat("dd MMM yyyy", Locale.US)
    return formatter.format(parser.parse(date)!!)
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val now = Date()
    return sdf.format(now)
}