package com.example.resipesdishesapp.data

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T): NetworkResult<T>()
    data class Error(val errorId: Int): NetworkResult<Nothing>()
}