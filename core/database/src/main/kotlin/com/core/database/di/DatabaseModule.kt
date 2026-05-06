package com.core.database.di

import androidx.room.Room
import com.core.database.StocksDatabase
import com.core.database.dao.FavoriteStockDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<StocksDatabase> {
        Room.databaseBuilder(androidContext(), StocksDatabase::class.java, "stocks_database")
            .build()
    }

    factory<FavoriteStockDao> { get<StocksDatabase>().favoriteStockDao() }
}
