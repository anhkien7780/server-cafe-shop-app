package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class User(
    val name: String,
    val age: Int,
    val birthDay: String,
    val sex: Boolean,
    val phoneNumber: String,
    val username: String
)

object Users: Table(){
    val userID = integer("userID").autoIncrement()
    val name = varchar("name", 50)
    val age = integer("age")
    val birthDay = varchar("birthDay", 12)
    val sex = bool("sex")
    val phoneNumber = varchar("phoneNumber", 11)
    val username = varchar("username", 30).references(Accounts.username)

    override val primaryKey = PrimaryKey(userID)
}
