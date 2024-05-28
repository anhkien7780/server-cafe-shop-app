package com.example.dao

import com.example.models.AvatarImage
import com.example.models.AvatarImages

interface AvatarImageDAO{
    suspend fun getAllImage(): List<AvatarImage>
    suspend fun addAvatarImage(username: String, url: String): AvatarImage?
    suspend fun getAvatarImage(username: String): AvatarImage?
    suspend fun changeAvatarImage(username: String, url: String): Boolean
}