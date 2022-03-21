package com.vajro.buygo.listeners

import com.vajro.buygo.models.ProductData

interface OnUpdateListener {
    fun onUpdate(prodId: Int, qty: Int)
    fun onInsert(model: ProductData)
}