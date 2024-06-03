package me.theek.spark.util

sealed class Endpoint(val route: String) {
    data object Welcome : Endpoint(route = "/v1/spark")
    data object Unauthorized : Endpoint(route = "/v1/spark/unauthorized")
    data object Artist : Endpoint(route = "/v1/spark/music/artist/{type}/{predicate}")
}