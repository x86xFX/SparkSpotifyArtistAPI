package me.theek.spark.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyAuthDto(
    val clientId: String,
    val accessToken: String,
    val accessTokenExpirationTimestampMs: Long,
    val isAnonymous: Boolean
)