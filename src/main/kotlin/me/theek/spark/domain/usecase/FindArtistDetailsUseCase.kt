package me.theek.spark.domain.usecase

import kotlinx.coroutines.*
import me.theek.spark.data.service.SpotifyArtistService
import me.theek.spark.domain.model.AccessKeyDetails
import me.theek.spark.domain.model.ArtistDetails
import me.theek.spark.domain.repository.StorageRepository
import me.theek.spark.util.Response

class FindArtistDetailsUseCase(
    private val spotifyArtistService: SpotifyArtistService,
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(predicate: String): Response<ArtistDetails> = withContext(Dispatchers.IO) {
        /**
         * Check expiration time in recent stored access token
         */
        val dbResponse = storageRepository.getUnauthorizedKey()
        when (dbResponse) {
            is Response.Failure -> {
                Response.Failure(dbResponse.message)
            }
            is Response.Success -> {
                /**
                 * Database transaction was success but result is null because database is empty somehow.
                 */
                if (dbResponse.data == null) {
                    val serverResponse = spotifyArtistService.getSpotifyUnauthorizedKey()
                    getGetArtistDetailsFromServers(
                        accessToken = serverResponse.accessToken,
                        expiresIn = serverResponse.accessTokenExpirationTimestampMs,
                        shouldStoreInDatabase = true,
                        artistName = predicate,
                    )

                } else if (isAccessTokenValid(dbResponse.data.expiresIn)) {
                    getGetArtistDetailsFromServers(
                        accessToken = dbResponse.data.accessToken,
                        expiresIn = dbResponse.data.expiresIn,
                        shouldStoreInDatabase = false,
                        artistName = predicate
                    )
                } else {
                    val serverResponse = spotifyArtistService.getSpotifyUnauthorizedKey()
                    getGetArtistDetailsFromServers(
                        accessToken = serverResponse.accessToken,
                        expiresIn = serverResponse.accessTokenExpirationTimestampMs,
                        shouldStoreInDatabase = true,
                        artistName = predicate,
                    )
                }
            }
        }
    }

    private suspend fun getGetArtistDetailsFromServers(
        accessToken: String,
        expiresIn: Long,
        shouldStoreInDatabase: Boolean,
        artistName: String
    ) : Response<ArtistDetails> {
        return try {
            /**
             * Store newly fetched access token details in mongodb
             */
            if (shouldStoreInDatabase) {
                coroutineScope {
                    launch(Dispatchers.IO) {
                        storageRepository.clear()
                        storageRepository.insertSpotifyUnauthorizedKey(
                            accessKeyDetails = AccessKeyDetails(
                                accessToken = accessToken,
                                expiresIn = expiresIn
                            )
                        )
                    }
                }
            }

            val artistData = spotifyArtistService.getSongArtistDetails(
                accessToken = accessToken,
                artistName = artistName
            )

            Response.Success(
                ArtistDetails(
                    artistName = artistData.artists.items[0].name,
                    followers = artistData.artists.items[0].followers.total,
                    artistGenres = artistData.artists.items[0].genres,
                    popularity = artistData.artists.items[0].popularity,
                    externalArtistProfile = artistData.artists.items[0].externalUrls.spotify,
                    imageUrl = artistData.artists.items[0].images[0].url
                )
            )

        } catch (e: Exception) {
            println(e.localizedMessage)
            Response.Failure(e.localizedMessage)
        }
    }

    private fun isAccessTokenValid(expiredIn: Long): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        val expiredInSeconds = expiredIn / 1000
        val buffer = 5000
        return currentTimeMillis + buffer <= expiredInSeconds * 1000
    }
}