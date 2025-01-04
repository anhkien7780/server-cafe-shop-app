package com.example.cloud

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import java.io.InputStream

object CloudService {
    val storage: Storage = StorageOptions.getDefaultInstance().service
    val bucketName = "kien-cafe-server.appspot.com" // Thay bằng tên bucket trên Google Cloud Storage

    fun saveImageToCloudStorage(fileName: String, fileContent: InputStream): String {
        val blobId = BlobId.of(bucketName, "avatar/$fileName") // Tệp lưu trong thư mục `avatar/`
        val blobInfo = BlobInfo.newBuilder(blobId).build()

        // Tải lên Cloud Storage
        storage.create(blobInfo, fileContent.readBytes())
        return "https://storage.googleapis.com/$bucketName/avatar/$fileName"
    }
}