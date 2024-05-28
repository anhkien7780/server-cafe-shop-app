package com.example.routes

import com.cafeshop.dao.dao
import com.example.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes() {
    route("/users") {
        get("{username?}") {
            val username = call.parameters["username"] ?: return@get call.respondText(
                "Missing username",
                status = HttpStatusCode.BadRequest
            )
            val user = dao.user(username) ?: return@get call.respondText(
                "Not found user with username $username",
                status = HttpStatusCode.NotFound
            )
            call.respond(user)
        }
        post("/change") {
            val user = call.receive<User>()
            dao.editUser(
                user.name,
                user.age,
                user.birthDay,
                user.sex,
                user.phoneNumber,
                user.username
            )
            call.respondText("Change user information success", status = HttpStatusCode.OK)
        }
        post("/add"){
            val user = call.receive<User>()
            dao.addNewUser(
                user.name,
                user.age,
                user.birthDay,
                user.sex,
                user.phoneNumber,
                user.username
            )
            call.respondText("Add user information success", status = HttpStatusCode.OK)
        }
    }
}