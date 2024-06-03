package me.theek.spark.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.theek.spark.util.Endpoint

fun Route.welcomeRoute() {
    get(Endpoint.Welcome.route) {
        call.respond(
            status = HttpStatusCode.OK,
            message = "Welcome to Spark Music Player API"
        )
    }

    post(Endpoint.Welcome.route) {
        call.respond(
            status = HttpStatusCode.OK,
            message = "Welcome to Spark Music Player API"
        )
    }
}