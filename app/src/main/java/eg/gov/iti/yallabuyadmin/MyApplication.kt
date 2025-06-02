package eg.gov.iti.yallabuyadmin

import android.app.Application
import eg.gov.iti.yallabuyadmin.di.appModule
import eg.gov.iti.yallabuyadmin.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(modules = arrayOf(appModule, viewModelsModule))
        }

    }

}