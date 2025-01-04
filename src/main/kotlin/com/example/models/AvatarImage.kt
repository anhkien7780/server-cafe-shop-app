package com.example.models

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import java.io.InputStream

@Serializable
data class AvatarImage(
    val url: String,
    val username: String
)

object AvatarImages : Table() {
    val url = varchar("avatarImageURL", 300)
    val username = varchar("username", 30).references(Accounts.username)
}




