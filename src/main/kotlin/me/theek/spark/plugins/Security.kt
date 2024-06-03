package me.theek.spark.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureSecurity() {
    authentication {
        basic(name = "spark_player_auth") {
            realm = "Ktor Server"
            validate { credentials ->
                if (credentials.name == System.getenv("SPARK_AUTH_USERNAME") && credentials.password == System.getenv("SPARK_AUTH_PASSWORD")) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}
