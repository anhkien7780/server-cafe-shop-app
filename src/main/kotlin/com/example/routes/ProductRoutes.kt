package com.example.routes

import com.cafeshop.dao.DAOFacadeImpl
import com.cafeshop.dao.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.productRouting(){
    route("/products"){
        get{
            val products = dao.allProducts()
            if (products.isNotEmpty()){
                call.respond(products)
            } else {
                call.respondText("No products found", status = HttpStatusCode.OK)
            }
        }
    }
}