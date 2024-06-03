package me.theek.spark.data.mapper

import me.theek.spark.data.entity.AccessTokenEntity
import me.theek.spark.domain.model.AccessKeyDetails

fun AccessTokenEntity.toAccessKeyDetails() : AccessKeyDetails {
    return AccessKeyDetails(
        accessToken = accessToken,
        expiresIn = accessTokenExpirationTimestampMs
    )
}