package com.example.dao

import com.example.CafeShopDatabase.dbQuery
import com.example.models.Cart
import com.example.models.CartItem
import com.example.models.CartItems
import com.example.models.Carts
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class CartDAOImpl : CartDAO {
    override suspend fun allCarts(): List<Cart> = dbQuery {
        Carts.selectAll().map { row ->
            val cartID = row[Carts.cartID]
            val cartItems = CartItems.select { CartItems.cartID eq cartID }.map {
                CartItem(
                    productID = it[CartItems.productID],
                    quantity = it[CartItems.quantity]
                )
            }
            Cart(
                cartID = cartID,
                listCartItem = cartItems
            )
        }
    }

    override suspend fun cart(username: String): Cart? = dbQuery {
        val cartID = Carts.select { Carts.username eq username }.singleOrNull()?.get(Carts.cartID)
        if (cartID != null) {
            val cartItems = CartItems.select { CartItems.cartID eq cartID }
            val listCartItems = cartItems.map {
                CartItem(
                    productID = it[CartItems.productID],
                    quantity = it[CartItems.quantity]
                )
            }
            Cart(
                cartID,
                listCartItems
            )
        } else
            null
    }

    override suspend fun getCartItem(cartID: Int, productID: Int): CartItem? = dbQuery {
        val resultRow =
            CartItems.select(where = (CartItems.cartID eq cartID) and (CartItems.productID eq productID)).singleOrNull()
        if (resultRow != null){
            CartItem(resultRow[CartItems.cartID], resultRow[CartItems.productID])
        } else
            null
    }

    override suspend fun addNewCart(username: String): Cart? = dbQuery {
        Carts.insert {
            it[Carts.username] = username
        }
        val cartID = Carts.select { Carts.username eq username }.singleOrNull()?.get(Carts.cartID)
        if (cartID != null) {
            Cart(cartID = cartID, listCartItem = emptyList())
        } else
            null
    }

    override suspend fun addCartItem(cartID: Int, productID: Int, quantity: Int): Boolean = dbQuery {
        CartItems.insert {
            it[CartItems.cartID] = cartID
            it[CartItems.productID] = productID
            it[CartItems.quantity] = quantity
        }
        val row =
            CartItems.select { (CartItems.cartID eq cartID) and (CartItems.productID eq productID) }.singleOrNull()
        row != null
    }

    override suspend fun deleteCartItem(cartID: Int, productID: Int): Boolean = dbQuery {
        val row = CartItems.select {
            (CartItems.cartID eq cartID) and (CartItems.productID eq productID)
        }.singleOrNull()

        if (row != null) {
            CartItems.deleteWhere {
                (CartItems.cartID eq cartID) and (CartItems.productID eq productID)
            }
            true
        } else {
            false
        }
    }

    override suspend fun deleteCart(cartID: Int): Boolean = dbQuery {

        val deletedRows = Carts.deleteWhere { Carts.cartID eq cartID }
        deletedRows > 0
    }

    override suspend fun updateCartItemQuantity(cartID: Int, productID: Int, newQuantity: Int): Boolean = dbQuery {
        val row = CartItems.update({ (CartItems.cartID eq cartID) and (CartItems.productID eq productID) }) {
            it[quantity] = newQuantity
        }
        row > 0
    }

    override suspend fun deleteAllCartItem(cartID: Int): Boolean = dbQuery {
        val deleteRows = CartItems.deleteWhere { CartItems.cartID eq cartID }
        deleteRows > 0
    }
}

val cartDAO = CartDAOImpl().apply {
    runBlocking {
        if (allCarts().isEmpty()) {
            addNewCart("admin@gmail.com")
        }
    }
}
