package com.vajro.buygo.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert
    fun insert(cart: Cart)

    @Query("UPDATE table_cart SET quantity=:qty WHERE product_id=:prod_id")
    fun update(qty: Int, prod_id: Int)

    @Query("DELETE FROM table_cart WHERE product_id=:prod_id")
    fun delete(prod_id: Int)

    @Query("DELETE FROM table_cart")
    fun deleteAllCartItems()

    @Query("SELECT * FROM table_cart")
    fun getAllCartItems(): Flow<List<Cart>>

}