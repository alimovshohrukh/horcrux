package uz.sicnt.eimzo

import android.app.Application
import android.content.Context
import uz.sicnt.horcrux.Horcrux

class MyApplication : Application() {
    companion object {
        lateinit var instance: Application
        lateinit var context: Context
        lateinit var horcrux: Horcrux
    }

    private val apiKey = "ВАШ API_KEY"

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        horcrux =
            Horcrux(
                context,
                apiKey
            )
    }
}