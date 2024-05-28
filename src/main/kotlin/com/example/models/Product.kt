package com.example.models


import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class Product(
    val productID: Int,
    val productName: String,
    val price: Float,
    val imgSrc: String,
    val description: String
)

object Products: Table(){
    val productID = integer("productID").autoIncrement()
    val productName = varchar("productName", 128)
    val price = float("price")
    val imgSrc = varchar("imageSource", 300)
    val description = varchar("description", 500)
    override val primaryKey = PrimaryKey(productID)
}
