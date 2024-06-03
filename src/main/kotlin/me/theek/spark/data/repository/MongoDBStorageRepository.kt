package me.theek.spark.data.repository

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.singleOrNull
import me.theek.spark.data.entity.AccessTokenEntity
import me.theek.spark.data.mapper.toAccessKeyDetails
import me.theek.spark.domain.model.AccessKeyDetails
import me.theek.spark.domain.repository.StorageRepository
import me.theek.spark.util.Response

class MongoDBStorageRepository(db: MongoDatabase) : StorageRepository {

    private val accessTokenCollection = db.getCollection<AccessTokenEntity>(collectionName = "AccessTokens")

    override suspend fun insertSpotifyUnauthorizedKey(accessKeyDetails: AccessKeyDetails) {
        accessTokenCollection.insertOne(
            document = AccessTokenEntity(
                accessToken = accessKeyDetails.accessToken,
                accessTokenExpirationTimestampMs = accessKeyDetails.expiresIn
            )
        )
    }

    override suspend fun getUnauthorizedKey(): Response<AccessKeyDetails?> {
        return try {
            Response.Success(accessTokenCollection.find().singleOrNull()?.toAccessKeyDetails())
        } catch (e: Exception) {
            Response.Failure(e.localizedMessage)
        }
    }

    override suspend fun clear() {
        accessTokenCollection.drop()
    }
}