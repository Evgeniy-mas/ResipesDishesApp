package com.example.resipesdishesapp.di

interface Factory<T> {
    fun create(): T
}