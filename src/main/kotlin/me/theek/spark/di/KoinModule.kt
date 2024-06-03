package me.theek.spark.di

import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import me.theek.spark.data.repository.MongoDBStorageRepository
import me.theek.spark.data.service.SpotifyArtistService
import me.theek.spark.domain.repository.StorageRepository
import me.theek.spark.domain.usecase.FindArtistDetailsUseCase
import org.koin.dsl.module

val KoinModule = module {
    single {
        MongoClient
            .create(connectionString = System.getenv("MONGODB_URI_CLUSTER"))
            .getDatabase(databaseName = "SparkMusicPlayerDB")
    }

    single {
        HttpClient(Android) {
            expectSuccess = true

            engine {
                connectTimeout = 10_000
                socketTimeout = 10_000
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    single<StorageRepository> {
        MongoDBStorageRepository(db = get())
    }

    single<SpotifyArtistService> {
        SpotifyArtistService(httpClient = get())
    }

    single<FindArtistDetailsUseCase> {
        FindArtistDetailsUseCase(
            spotifyArtistService = get(),
            storageRepository = get()
        )
    }
}