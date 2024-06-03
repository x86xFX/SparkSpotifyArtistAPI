package me.theek.spark.plugins

import io.ktor.server.application.*
import me.theek.spark.di.KoinModule
import org.koin.ktor.plugin.Koin

fun Application.configureKoinModule() {
    install(Koin) {
        modules(KoinModule)
    }
}