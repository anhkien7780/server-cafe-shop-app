package com.example.dao

import com.example.models.Invoice

interface InvoiceDAO {
    suspend fun invoice(username: String, invoiceID: Int): Invoice?
    suspend fun invoices(username: String): List<Invoice>
    suspend fun addInvoice(username: String, subTotal: Float, date: String, address: String, paid: Boolean): Boolean
    suspend fun addInvoiceAndGetId(username: String, subTotal: Float, date: String, address: String, paid: Boolean): Int
    suspend fun changeToPaidInvoice(username: String, invoiceID: Int): Boolean
}

interface InvoicesProductsDAO {
    suspend fun invoicesProductID(invoiceID: Int): List<Int>
    suspend fun addProductID(invoiceID: Int, productID: Int): Boolean
}