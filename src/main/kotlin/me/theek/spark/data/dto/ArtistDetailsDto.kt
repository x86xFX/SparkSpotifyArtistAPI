package me.theek.spark.data.dto

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDetailsDto(
    val artists: Artists
) {
    @Serializable
    data class Artists(
        val href: String,
        val items: List<ArtistData>,
        val limit: Int,
        @Required val next: String?,
        val offset: Int,
        @Required val previous: String?,
        val total: Int
    ) {
        @Serializable
        data class ArtistData(
            @SerialName("external_urls") val externalUrls: ExternalUrls,
            val followers: Followers,
            val genres: List<String>,
            val href: String,
            val id: String,
            val images: List<ArtistImage>,
            val name: String,
            val popularity: Int,
            val type: String,
            val uri: String
        ) {
            @Serializable
            data class ExternalUrls(
                val spotify: String
            )
            @Serializable
            data class Followers(
                @Required val href: String?,
                val total: Int
            )
            @Serializable
            data class ArtistImage(
                @Required val height: Int?,
                val url: String,
                @Required val width: Int?,
            )
        }
    }
}