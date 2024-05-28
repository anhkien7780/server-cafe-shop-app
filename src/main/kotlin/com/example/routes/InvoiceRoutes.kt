package com.example.routes

import com.example.dao.invoicesDao
import com.example.dao.invoicesProductsDao
import com.example.models.Invoice
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.invoiceRoutes() {
    route("/invoices") {
        get("/get/{username?}") {
            val username = call.parameters["username"] ?: return@get call.respondText(
                "Missing username to get invoices",
                status = HttpStatusCode.BadRequest
            )
            val invoices = invoicesDao.invoices(username)
            call.respond(invoices)
        }
        post("/add") {
            val invoice = call.receive<Invoice>()
            val invoiceID = invoicesDao.addInvoiceAndGetId(
                username = invoice.username,
                subTotal = invoice.subTotal,
                date = invoice.date,
                address = invoice.address,
                paid = invoice.paid
            )
            for (productID in invoice.listProductID) {
                invoicesProductsDao.addProductID(productID = productID, invoiceID = invoiceID)
            }
            call.respondText("Add invoice success", status = HttpStatusCode.Created)
        }
    }
}