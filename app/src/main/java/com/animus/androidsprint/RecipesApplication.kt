package com.animus.androidsprint

import android.app.Application
import com.animus.androidsprint.di.AppContainer

class RecipesApplication: Application() {

    lateinit var appConteiner: AppContainer

    override fun onCreate() {
        super.onCreate()

        appConteiner = AppContainer(this)
    }
}