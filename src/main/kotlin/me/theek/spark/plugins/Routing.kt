package me.theek.spark.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import me.theek.spark.domain.usecase.FindArtistDetailsUseCase
import me.theek.spark.route.getArtistDetailsRoute
import me.theek.spark.route.unauthorizedRoute
import me.theek.spark.route.welcomeRoute
import org.koin.java.KoinJavaComponent

fun Application.configureRouting() {

    val findArtistDetailsUseCase: FindArtistDetailsUseCase by KoinJavaComponent.inject(FindArtistDetailsUseCase::class.java)

    routing {
        welcomeRoute()
        getArtistDetailsRoute(findArtistDetailsUseCase = findArtistDetailsUseCase)
        unauthorizedRoute()
    }
}
