package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Address(
    val addressID: Int,
    val userID: Int,
    val address: String
)

object Addresses: Table(){
    val addressID = integer("addressID").autoIncrement()
    val userID = integer("userID").references(Users.userID)
    val address = varchar("address", 200)

    override val primaryKey = PrimaryKey(addressID)
}
