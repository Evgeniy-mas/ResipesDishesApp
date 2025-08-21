package com.example.resipesdishesapp

import android.app.Application
import com.example.resipesdishesapp.di.AppContainer

class RecipesApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)
    }
}