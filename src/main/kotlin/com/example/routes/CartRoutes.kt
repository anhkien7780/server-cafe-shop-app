package com.example.routes

import com.example.dao.cartDAO
import com.example.models.Cart
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.cartRoutes() {
    route("/cart") {
        get("/{username?}") {
            val username = call.parameters["username"] ?: return@get call.respondText(
                "Missing username for get cart",
                status = HttpStatusCode.BadRequest
            )
            val cart: Cart = cartDAO.cart(username) ?: return@get call.respondText(
                "Not found cart",
                status = HttpStatusCode.NotFound
            )
            call.respond(cart)
        }
        get("/add/{username?}") {
            val username = call.parameters["username"] ?: return@get call.respondText(
                "Missing username for get cart",
                status = HttpStatusCode.BadRequest
            )
            cartDAO.addNewCart(username)
            val cart: Cart = cartDAO.cart(username) ?: return@get call.respondText(
                "Not found cart",
                status = HttpStatusCode.NotFound
            )
            call.respond(cart)
        }
        post("/add/{cartID?}/{productID?}") {
            val cartID = call.parameters["cartID"] ?: return@post call.respondText(
                "Missing cartID for update cart",
                status = HttpStatusCode.BadRequest
            )
            val productID = call.parameters["productID"] ?: return@post call.respondText(
                "Missing productID for update cart",
                status = HttpStatusCode.BadRequest
            )
            val existsCartItem = cartDAO.getCartItem(cartID.toInt(), productID.toInt())
            if (existsCartItem != null) {
                cartDAO.updateCartItemQuantity(cartID.toInt(), productID.toInt(), existsCartItem.quantity + 1)
                call.respondText("Add to exists productID", status = HttpStatusCode.OK)
            } else {
                cartDAO.addCartItem(
                    cartID = cartID.toInt(),
                    productID = productID.toInt(),
                    1
                )

                call.respondText(
                    "Add cart item success",
                    status = HttpStatusCode.OK
                )
            }

        }
        post("/update/{cartID?}/{productID?}/{quantity?}") {
            val cartID = call.parameters["cartID"] ?: return@post call.respondText(
                "Missing cartID for update cart",
                status = HttpStatusCode.BadRequest
            )
            val productID = call.parameters["productID"] ?: return@post call.respondText(
                "Missing productID for update cart",
                status = HttpStatusCode.BadRequest
            )
            val quantity = call.parameters["quantity"] ?: return@post call.respondText(
                "Missing quantity for update cart",
                status = HttpStatusCode.BadRequest
            )
            cartDAO.updateCartItemQuantity(
                cartID = cartID.toInt(),
                productID = productID.toInt(),
                newQuantity = quantity.toInt()
            )
            call.respondText("Update date cart successful", status = HttpStatusCode.OK)
        }
        delete("/delete/{cartID?}/{productID?}") {
            val cartID = call.parameters["cartID"] ?: return@delete call.respondText(
                "Missing cartID for update cart",
                status = HttpStatusCode.BadRequest
            )
            val productID = call.parameters["productID"] ?: return@delete call.respondText(
                "Missing productID for update cart",
                status = HttpStatusCode.BadRequest
            )
            cartDAO.deleteCartItem(cartID.toInt(), productID.toInt())
            call.respondText("Delete cart item successful")
        }
        delete("/delete/{cartID?}") {
            val cartID = call.parameters["cartID"] ?: return@delete call.respondText(
                "Missing cartID for update cart",
                status = HttpStatusCode.BadRequest
            )
            cartDAO.deleteAllCartItem(cartID.toInt())
            call.respondText("Delete cart successful", status = HttpStatusCode.OK)
        }

    }
}