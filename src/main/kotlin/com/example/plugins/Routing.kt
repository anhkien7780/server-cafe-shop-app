package com.example.plugins

import com.example.routes.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
    routing {
        productRouting()
        accountRouting()
        userRoutes()
        cartRoutes()
        avatarImageRoutes()
        invoiceRoutes()
    }
}
