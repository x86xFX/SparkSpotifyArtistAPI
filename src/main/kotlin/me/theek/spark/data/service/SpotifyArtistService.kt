package me.theek.spark.data.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import me.theek.spark.data.dto.ArtistDetailsDto
import me.theek.spark.data.dto.SpotifyAuthDto

class SpotifyArtistService(private val httpClient: HttpClient) {

    /**
     * https://open.spotify.com/get_access_token?reason=transport&productType=web_player
     */
    suspend fun getSpotifyUnauthorizedKey(): SpotifyAuthDto {
        return httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "open.spotify.com"
                path("get_access_token")
                parameters.append(name = "reason", value = "transport")
                parameter(key = "productType", value = "web_player")
            }
        }.body<SpotifyAuthDto>()
    }

    /**
     * https://api.spotify.com/v1/search?type=artist&q=Ariana&decorate_restrictions=false&limit=1
     */
    suspend fun getSongArtistDetails(
        accessToken: String,
        artistName: String
    ): ArtistDetailsDto {
        return httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "api.spotify.com"
                bearerAuth(accessToken)
                path("v1", "search")
                parameters.append(name = "type", value = "artist")
                parameter(key = "q", value = artistName)
                parameter(key = "decorate_restrictions", value = false)
                parameter(key = "limit", value = 1)
            }
        }.body<ArtistDetailsDto>()
    }
}