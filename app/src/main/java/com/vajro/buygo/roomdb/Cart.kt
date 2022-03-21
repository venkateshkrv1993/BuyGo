package com.vajro.buygo.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_cart")
data class Cart(
    val name: String,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val product_id: Int,
    val sku: String,
    val image: String,
    val thumb: String,
    val zoom_thumb: String,
    val description: String,
    var quantity: Int,
    val price: String,
    val special: String,
)
