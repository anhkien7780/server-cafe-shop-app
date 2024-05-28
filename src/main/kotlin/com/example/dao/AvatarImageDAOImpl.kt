package com.example.dao

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.ExifIFD0Directory
import com.example.CafeShopDatabase.dbQuery

import com.example.models.AvatarImage
import com.example.models.AvatarImages
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.io.File

class AvatarImageDAOImpl : AvatarImageDAO {
    private fun resultRowToImage(row: ResultRow) = AvatarImage(
        username = row[AvatarImages.username],
        url = row[AvatarImages.url]
    )

    fun rotateImage(image: BufferedImage, angle: Double): BufferedImage {
        val transform = AffineTransform.getRotateInstance(Math.toRadians(angle), image.width / 2.0, image.height / 2.0)
        val op = AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR)
        return op.filter(image, null)
    }

    fun extractOrientation(file: File): Int? {
        val metadata = ImageMetadataReader.readMetadata(file)
        val directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory::class.java)
        return directory?.getInt(ExifIFD0Directory.TAG_ORIENTATION)
    }

    override suspend fun getAllImage(): List<AvatarImage> = dbQuery {
        AvatarImages.selectAll().map(::resultRowToImage)
    }

    override suspend fun addAvatarImage(username: String, url: String): AvatarImage? = dbQuery {
        val insertStatement = AvatarImages.insert {
            it[AvatarImages.username] = username
            it[AvatarImages.url] = url
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToImage)
    }

    override suspend fun getAvatarImage(username: String): AvatarImage? = dbQuery {
        AvatarImages.select(AvatarImages.username eq username).map(::resultRowToImage).firstOrNull()
    }

    override suspend fun changeAvatarImage(username: String, url: String): Boolean = dbQuery {
        AvatarImages.update({AvatarImages.username eq username}){
            it[AvatarImages.url] = url
        } > 0
    }
}

val avatarImageDAOImpl = AvatarImageDAOImpl().apply {
    runBlocking {
        if (getAllImage().isEmpty()){
            addAvatarImage("admin@gmail.com", "https://png.pngtree.com/png-clipart/20200224/original/pngtree-cartoon-color-simple-male-avatar-png-image_5230557.jpg")
        }
    }
}
