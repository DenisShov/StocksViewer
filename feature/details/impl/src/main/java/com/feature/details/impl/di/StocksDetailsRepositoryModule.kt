package com.feature.details.impl.di

import com.feature.details.impl.data.repository.StocksDetailsRepositoryImpl
import com.feature.details.impl.domain.repository.StocksDetailsRepository
import com.feature.details.impl.domain.usecase.GetStockChartDataUseCase
import com.feature.details.impl.ui.StockDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val stockDetailsModule = module {
    factory<StocksDetailsRepository> { StocksDetailsRepositoryImpl(get()) }
    factory { GetStockChartDataUseCase(get()) }
    viewModel { params ->
        StockDetailsViewModel(
            ticker = params.get(),
            stocksDetailsRepository = get(),
            getStockChartDataUseCase = get(),
            favoritesRepository = get(),
            errorMapper = get(),
        )
    }
}
