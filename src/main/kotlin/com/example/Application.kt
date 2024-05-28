package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    CafeShopDatabase.init()
//    configureTemplating()
    configureSerialization()
    configureRouting()
}
