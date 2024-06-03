package me.theek.spark.domain.model

data class ArtistDetails(
    val artistName: String,
    val followers: Int,
    val popularity: Int,
    val artistGenres: List<String>,
    val externalArtistProfile: String,
    val imageUrl: String
)