package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Account(
    val username: String,
    val password: String,
)

object Accounts: Table(){
    val username = varchar("username", 30)
    val password = varchar("password", 30)

    override val primaryKey = PrimaryKey(username)
}