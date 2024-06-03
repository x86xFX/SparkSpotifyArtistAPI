package me.theek.spark.domain.repository

import me.theek.spark.domain.model.AccessKeyDetails
import me.theek.spark.util.Response

interface StorageRepository {
    suspend fun insertSpotifyUnauthorizedKey(accessKeyDetails: AccessKeyDetails)
    suspend fun getUnauthorizedKey() : Response<AccessKeyDetails?>
    suspend fun clear()
}