package com.core.database.di

import android.content.Context
import androidx.room.Room
import com.core.database.StocksDatabase
import com.core.database.dao.FavoriteStockDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): StocksDatabase =
        Room.databaseBuilder(context, StocksDatabase::class.java, "stocks_database")
            .build()

    @Provides
    fun provideFavoriteStockDao(database: StocksDatabase): FavoriteStockDao =
        database.favoriteStockDao()
}
