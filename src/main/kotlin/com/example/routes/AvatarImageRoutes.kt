package com.example.routes

import com.example.cloud.CloudService.saveImageToCloudStorage
import com.example.dao.avatarImageDAOImpl
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.avatarImageRoutes() {
    route("/avatar") {
        post("/add/{username?}") {
            val username = call.parameters["username"] ?: return@post call.respondText(
                "Missing username",
                status = HttpStatusCode.BadRequest
            )
            val multipart = call.receiveMultipart()
            var imageUrl: String? = null
            multipart.forEachPart { part ->
                if (part is PartData.FileItem) {
                    val fileName = part.originalFileName ?: "$username.png"
                    imageUrl = saveImageToCloudStorage(fileName, part.streamProvider())
                }
                part.dispose()
                if (imageUrl != null) {
                    avatarImageDAOImpl.addAvatarImage(username = username, url = imageUrl)
                    call.respondText("Avatar uploaded successfully: $imageUrl")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Failed to upload avatar")
                }
            }
        }
        post("/change/{username?}") {
            val username = call.parameters["username"] ?: return@post call.respondText(
                "Missing username",
                status = HttpStatusCode.BadRequest
            )
            val multipart = call.receiveMultipart()
            var imageUrl: String? = null
            multipart.forEachPart { part ->
                if (part is PartData.FileItem) {
                    val fileName = part.originalFileName ?: "$username.png"
                    imageUrl = saveImageToCloudStorage(fileName, part.streamProvider())
                }
                part.dispose()
                if (imageUrl != null) {
                    avatarImageDAOImpl.changeAvatarImage(username = username, url = imageUrl)
                    call.respondText("Avatar uploaded successfully: $imageUrl")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Failed to upload avatar")
                }
            }
        }
        get("/get/{username}") {
            val username = call.parameters["username"] ?: return@get call.respondText(
                "Missing username for get image",
                status = HttpStatusCode.BadRequest
            )
            val avatarImage = avatarImageDAOImpl.getAvatarImage(username) ?: return@get call.respondText(
                "Not found avatar image",
                status = HttpStatusCode.NotFound
            )
            call.respond(avatarImage)
        }
    }
}