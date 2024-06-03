package me.theek.spark.data.entity

data class AccessTokenEntity(
    val accessToken: String,
    val accessTokenExpirationTimestampMs: Long,
    val insertedIn: Long = System.currentTimeMillis()
)
