package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Cart(
    val cartID: Int,
    val listCartItem: List<CartItem>
)


@Serializable
data class CartItem(
    val productID: Int,
    val quantity: Int
)

object Carts: Table(){
    val cartID = integer("cartID").autoIncrement()
    val username = varchar("username", 30).references(Accounts.username)

    override val primaryKey = PrimaryKey(cartID)
}

object CartItems: Table(){
    val cartID = integer("cartID").references(Carts.cartID)
    val productID = integer("productID").references(Products.productID)
    val quantity = integer("quantity")
}