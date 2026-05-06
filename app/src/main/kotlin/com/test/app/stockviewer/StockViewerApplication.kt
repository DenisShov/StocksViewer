package com.test.app.stockviewer

import android.app.Application
import com.core.common.di.commonModule
import com.core.database.di.databaseModule
import com.core.network.di.networkModule
import com.feature.details.impl.di.stockDetailsModule
import com.feature.favorites.impl.di.favoritesListModule
import com.feature.list.impl.di.stocksListModule
import com.sharedlibrary.favorites.di.favoritesModule
import core.commonresources.di.commonResourcesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class StockViewerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@StockViewerApplication)
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            modules(
                networkModule,
                databaseModule,
                commonResourcesModule,
                commonModule,
                favoritesModule,
                stocksListModule,
                stockDetailsModule,
                favoritesListModule,
            )
        }
    }
}
