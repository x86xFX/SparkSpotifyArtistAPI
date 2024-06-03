package me.theek.spark.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.theek.spark.data.dto.ArtistDetailsResponse
import me.theek.spark.domain.usecase.FindArtistDetailsUseCase
import me.theek.spark.util.Endpoint
import me.theek.spark.util.Response

fun Route.getArtistDetailsRoute(findArtistDetailsUseCase: FindArtistDetailsUseCase) {
    authenticate("spark_player_auth") {
        get(Endpoint.Artist.route) {
            val typeParam = call.parameters["type"]
            val predicateParam = call.parameters["predicate"]

            /**
             * Client response filter. If type is image, response is artist image without artist spotify details.
             * Available types: ["profile", "details"]
             */

            if (predicateParam == null) {
                call.respondRedirect(Endpoint.Unauthorized.route)
                return@get
            }

            if (predicateParam == "<unknown>" && typeParam == "profile") {
                call.respondRedirect("https://images.genius.com/393bfda8da80c5024e572eb01cf58020.900x900x1.jpg")
            } else {
                when (val response = findArtistDetailsUseCase(predicateParam)) {
                    is Response.Failure -> {
                        call.respond(
                            status = HttpStatusCode.NotFound,
                            message = "Not Found"
                        )
                    }
                    is Response.Success -> {
                        when (typeParam) {
                            "profile" -> {
                                call.respondRedirect(response.data.imageUrl)
                            }
                            "details" -> {
                                val artistData = response.data
                                call.respond(
                                    status = HttpStatusCode.OK,
                                    message = ArtistDetailsResponse(
                                        artistName = artistData.artistName,
                                        followers = artistData.followers,
                                        popularity = artistData.popularity,
                                        artistGenres = artistData.artistGenres,
                                        externalArtistProfile = artistData.externalArtistProfile,
                                        imageUrl = artistData.imageUrl
                                    )
                                )
                            }
                            else -> {
                                call.respond(
                                    status = HttpStatusCode.InternalServerError,
                                    message = "Something went wrong."
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}