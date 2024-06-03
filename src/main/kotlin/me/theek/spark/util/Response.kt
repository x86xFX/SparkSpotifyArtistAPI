package me.theek.spark.util

sealed interface Response<T> {
    data class Failure<T>(val message: String?) : Response<T>
    data class Success<T>(val data: T) : Response<T>
}