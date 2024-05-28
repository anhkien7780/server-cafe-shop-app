package com.example.routes

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Route.imageRoutes() {
    route("/image") {
        post("/upload") {
            val multipart = call.receiveMultipart()
            multipart.forEachPart { part ->
                if (part is PartData.FileItem) {
                    val fileBytes = part.streamProvider().readBytes()
                    val file = File("./uploads/${part.originalFileName}")
                    file.writeBytes(fileBytes)
                    call.respond(HttpStatusCode.OK, "http://localhost:8080/uploads/${file.name}")
                }
                part.dispose()
            }
        }
    }
}
