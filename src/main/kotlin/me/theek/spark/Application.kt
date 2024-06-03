package me.theek.spark

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.theek.spark.plugins.*

fun main() {
    embeddedServer(
        factory = Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    configureKoinModule()
    configureSecurity()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
