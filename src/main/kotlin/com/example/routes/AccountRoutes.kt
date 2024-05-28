package com.example.routes

import com.cafeshop.dao.dao
import com.example.models.Account
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.accountRouting() {
    route("/accounts") {
        post("/login") {
            try {
                val account: Account = call.receive<Account>()
                if (dao.allAccounts().contains(account)) {
                    call.respond(HttpStatusCode.OK, "Login successful")
                } else call.respond(HttpStatusCode.BadRequest, "Invalid credentials")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }
        post("/register") {
            try {
                val account: Account = call.receive<Account>()
                if (account.username.isNotBlank() && account.password.isNotBlank()) {
                    dao.addNewAccount(account.username, account.password)
                    call.respond(HttpStatusCode.OK, "Account created successfully")
                } else call.respond(HttpStatusCode.BadRequest, "Username and password must not be blank")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
            call.respondText(
                "Register successfully",
                status = HttpStatusCode.Created
            )
        }
        post("/change-password/{oldPassword?}") {
            try {
                val oldPassword = call.parameters["oldPassword"] ?: return@post call.respondText(
                    "Missing old password to change password",
                    status = HttpStatusCode.BadRequest
                )
                val account: Account = call.receive<Account>()
                if (account.username.isNotBlank() && account.password.isNotBlank()) {
                    if (dao.account(username = account.username)?.password == oldPassword) {
                        dao.changePassword(account.username, account.password)
                        call.respond(HttpStatusCode.OK, "Password changed successfully")
                    } else{
                        return@post call.respondText(
                            "Wrong password",
                            status = HttpStatusCode.NotFound
                        )
                    }
                } else call.respond(HttpStatusCode.BadRequest, "Username and password must not be blank")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "An error occurred: ${e.message}")
            }
        }
    }
}