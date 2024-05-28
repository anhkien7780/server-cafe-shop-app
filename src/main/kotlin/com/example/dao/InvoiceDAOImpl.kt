package com.example.dao

import com.example.CafeShopDatabase.dbQuery
import com.example.models.Invoice
import com.example.models.Invoices
import com.example.models.InvoicesProducts
import org.jetbrains.exposed.sql.*

class InvoiceDAOImpl : InvoiceDAO {
    override suspend fun invoice(username: String, invoiceID: Int): Invoice? = dbQuery {
        val listProductID = invoicesProductsDao.invoicesProductID(invoiceID)
        Invoices.select { (Invoices.id eq invoiceID) and (Invoices.username eq username) }.singleOrNull()?.let { row ->
            Invoice(
                date = row[Invoices.date],
                paid = row[Invoices.paid],
                subTotal = row[Invoices.subTotal],
                address = row[Invoices.address],
                username = row[Invoices.username],
                listProductID = listProductID
            )
        }
    }

    override suspend fun invoices(username: String): List<Invoice> = dbQuery {
        val listInvoiceID = Invoices.select { Invoices.username eq username }.map { it[Invoices.id] }
        val listInvoice = mutableListOf<Invoice>()
        for (invoiceID in listInvoiceID) {
            val listInvoiceProducts = invoicesProductsDao.invoicesProductID(invoiceID.value)
            val selectStatement = Invoices.select { Invoices.id eq invoiceID }
            val invoice = selectStatement.singleOrNull()?.let { row ->
                Invoice(
                    date = row[Invoices.date],
                    paid = row[Invoices.paid],
                    subTotal = row[Invoices.subTotal],
                    address = row[Invoices.address],
                    username = row[Invoices.username],
                    listProductID = listInvoiceProducts
                )
            }
            if (invoice != null) {
                listInvoice.add(invoice)
            }
        }
        listInvoice
    }

    override suspend fun addInvoice(
        username: String, subTotal: Float, date: String, address: String, paid: Boolean
    ): Boolean = dbQuery{
        try {
            Invoices.insert {
                it[Invoices.username] = username
                it[Invoices.subTotal] = subTotal
                it[Invoices.date] = date
                it[Invoices.address] = address
                it[Invoices.paid] = paid
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun addInvoiceAndGetId(
        username: String, subTotal: Float, date: String, address: String, paid: Boolean
    ): Int = dbQuery{
        try {
            val id = Invoices.insertAndGetId {
                it[Invoices.username] = username
                it[Invoices.subTotal] = subTotal
                it[Invoices.date] = date
                it[Invoices.address] = address
                it[Invoices.paid] = paid
            }
            id.value
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    override suspend fun changeToPaidInvoice(username: String, invoiceID: Int): Boolean = dbQuery{
        try {
            Invoices.update(where = { (Invoices.id eq invoiceID) and (Invoices.username eq username) }) {
                it[paid] = paid
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

class InvoicesProductsDAOImpl : InvoicesProductsDAO {
    override suspend fun invoicesProductID(invoiceID: Int): List<Int> = dbQuery{
        InvoicesProducts.select { InvoicesProducts.invoiceID eq invoiceID }
            .map { it[InvoicesProducts.productID] }
    }


    override suspend fun addProductID(invoiceID: Int, productID: Int): Boolean = dbQuery {
        try {
            InvoicesProducts.insert {
                it[InvoicesProducts.invoiceID] = invoiceID
                it[InvoicesProducts.productID] = productID
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

val invoicesProductsDao = InvoicesProductsDAOImpl()
val invoicesDao = InvoiceDAOImpl()