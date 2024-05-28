package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Invoice(
    val subTotal: Float,
    val date: String,
    val paid: Boolean,
    val address: String,
    val listProductID: List<Int>,
    val username: String
)


object Invoices : IntIdTable() {
    val subTotal = float("subTotal")
    val date = varchar("date", 10)
    val address = varchar("address", 300)
    val paid = bool("paid")
    val username = varchar("username", 30).references(Accounts.username)

}

object InvoicesProducts: Table(){
    val invoiceID = integer("invoiceID").references(Invoices.id)
    val productID = integer("productID").references(Products.productID)
}

