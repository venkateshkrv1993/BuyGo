package com.vajro.buygo

import android.app.Application
import com.vajro.buygo.dagger.DaggerMainComponent
import com.vajro.buygo.dagger.MainComponent

class MainApplication : Application() {

    lateinit var mainComponent: MainComponent


    companion object {
        lateinit var application: MainApplication
        fun getMainApplication(): MainApplication {
            return application
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        mainComponent = DaggerMainComponent.builder().build()
    }

}