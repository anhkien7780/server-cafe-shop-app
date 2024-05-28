package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

@Serializable
data class AvatarImage(
    val url: String,
    val username: String
)

object AvatarImages : Table() {
    val url = varchar("avatarImageURL", 300)
    val username = varchar("username", 30).references(Accounts.username)
}