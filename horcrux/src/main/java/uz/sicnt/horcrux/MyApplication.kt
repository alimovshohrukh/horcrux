package uz.sicnt.horcrux

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    companion object {
        lateinit var instance: Application
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext

    }
}