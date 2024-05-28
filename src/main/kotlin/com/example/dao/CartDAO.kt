package com.example.dao

import com.example.models.Cart
import com.example.models.CartItem
import com.example.models.CartItems

interface CartDAO {
    suspend fun allCarts(): List<Cart>
    suspend fun cart(username: String): Cart?
    suspend fun addNewCart(username: String): Cart?
    suspend fun getCartItem(cartID: Int, productID: Int): CartItem?
    suspend fun addCartItem(cartID: Int, productID: Int, quantity: Int): Boolean
    suspend fun deleteCartItem(cartID: Int, productID: Int): Boolean
    suspend fun deleteCart(cartID: Int): Boolean
    suspend fun updateCartItemQuantity(cartID: Int, productID: Int, newQuantity: Int): Boolean
    suspend fun deleteAllCartItem(cartID: Int): Boolean
}