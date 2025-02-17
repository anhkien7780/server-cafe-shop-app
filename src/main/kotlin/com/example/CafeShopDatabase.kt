package com.example

import com.example.models.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object CafeShopDatabase {
    fun init(){
        val driverClassName = "org.h2.Driver"
        val jdbcUrl = "jdbc:h2:file:./build.db"
        val database = Database.connect(jdbcUrl, driverClassName)
        transaction (database){
            SchemaUtils.create(Products)
            SchemaUtils.create(Accounts)
            SchemaUtils.create(Users)
            SchemaUtils.create(Addresses)
            SchemaUtils.create(Carts)
            SchemaUtils.create(CartItems)
            SchemaUtils.create(AvatarImages)
            SchemaUtils.create(Invoices)
            SchemaUtils.create(InvoicesProducts)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}