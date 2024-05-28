package com.example.routes

import com.example.dao.avatarImageDAOImpl
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun Route.avatarImageRoutes() {
    route("/avatar") {
        post("/add/{username?}") {
            val username = call.parameters["username"] ?: return@post call.respondText(
                "Missing username",
                status = HttpStatusCode.BadRequest
            )
            val ipv4NPort = "https://one-definitely-kingfish.ngrok-free.app"
            val absoluteAvatarFolderPath =
                "C:/Users/anhki/Documents/Learn/cafe-shop/src/main/kotlin/com/example/avatarImages/"
            val multipart = call.receiveMultipart()
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> TODO()
                    is PartData.BinaryChannelItem -> TODO()
                    is PartData.BinaryItem -> TODO()
                    is PartData.FileItem -> {
                        val originalFileName = part.originalFileName ?: System.currentTimeMillis().toString()
                        val bytes = part.streamProvider().readBytes()
                        val path: Path =
                            Paths.get(absoluteAvatarFolderPath + originalFileName)
                        Files.write(path, bytes)
                        val relativeImagePath = "/avatar/display/"
                        val imageUrl =
                            ipv4NPort + relativeImagePath + originalFileName
                        avatarImageDAOImpl.addAvatarImage(username = username, url = imageUrl)
                    }
                }
                part.dispose()
                call.respondText("Added avatar image", status = HttpStatusCode.OK)
            }
        }
        post("/change/{username?}") {
            val username = call.parameters["username"] ?: return@post call.respondText(
                "Missing username",
                status = HttpStatusCode.BadRequest
            )
            val ipv4NPort = "https://one-definitely-kingfish.ngrok-free.app"
            val absoluteAvatarFolderPath =
                "C:/Users/anhki/Documents/Learn/cafe-shop/src/main/kotlin/com/example/avatarImages/"
            val multipart = call.receiveMultipart()
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> TODO()
                    is PartData.BinaryChannelItem -> TODO()
                    is PartData.BinaryItem -> TODO()
                    is PartData.FileItem -> {
                        val originalFileName = part.originalFileName ?: System.currentTimeMillis().toString()
                        val bytes = part.streamProvider().readBytes()
                        val path: Path =
                            Paths.get(absoluteAvatarFolderPath + originalFileName)
                        Files.write(path, bytes)
                        val relativeImagePath = "/avatar/display/"
                        val imageUrl =
                            ipv4NPort + relativeImagePath + originalFileName
                        avatarImageDAOImpl.changeAvatarImage(username = username, url = imageUrl)
                    }
                }
                part.dispose()
                call.respondText("Change avatar image", status = HttpStatusCode.OK)
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