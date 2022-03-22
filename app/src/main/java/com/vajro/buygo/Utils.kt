package com.vajro.buygo

import java.text.DecimalFormat

object Utils {

    const val rs = "\u20B9"
    private const val format = "##,##,###"
    private const val formatZero = "##,##,###.##"

    fun convertAmount(price: String): Double {
        val amt = price.replace(rs, "").replace(",", "").trim()
        return if (amt.isNotEmpty()) amt.toDouble()
        else 0.0
    }

    fun currencyFormat(value: Double, withDecimal: Boolean = false): String {
        if (withDecimal) String.format("%s %s", rs, DecimalFormat(formatZero).format(value))
        return String.format("%s %s", rs, DecimalFormat(format).format(value))
    }

}