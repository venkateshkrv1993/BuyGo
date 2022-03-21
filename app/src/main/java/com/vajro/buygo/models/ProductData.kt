package com.vajro.buygo.models

data class ProductData(
    val name: String,
    val id: Int,
    val product_id: Int,
    val sku: String,
    val image: String,
    val thumb: String,
    val zoom_thumb: String,
    val options: ArrayList<Any>,
    val description: String,
    val href: String,
    val quantity: Int,
    val images: ArrayList<String>,
    val price: String,
    val special: String,
    var qty: Int = 0
) {

}