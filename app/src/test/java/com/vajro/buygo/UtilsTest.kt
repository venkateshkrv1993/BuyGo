package com.vajro.buygo

import com.google.common.truth.Truth
import org.junit.Test

class UtilsTest {

    @Test
    fun `convert Amount Test`() {
        val result = Utils.convertAmount("12,00,000")
        Truth.assertThat(result).isEqualTo(1200000.0)
    }

    @Test
    fun `currency Format Test`() {
        val amt = Utils.convertAmount("1200")
        val result = Utils.currencyFormat(amt, false)
        Truth.assertThat(result).isEqualTo(Utils.rs + " " + "1,200")
    }

}