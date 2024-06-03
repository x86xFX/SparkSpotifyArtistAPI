package me.theek.spark.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.theek.spark.util.Endpoint

fun Route.unauthorizedRoute() {
    post(Endpoint.Unauthorized.route) {
        call.respond(
            status = HttpStatusCode.Unauthorized,
            message = "Unauthorized"
        )
    }

    get(Endpoint.Unauthorized.route) {
        call.respond(
            status = HttpStatusCode.Unauthorized,
            message = "Unauthorized"
        )
    }
}