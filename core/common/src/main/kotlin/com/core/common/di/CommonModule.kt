package com.core.common.di

import com.core.common.mapper.ErrorMapper
import org.koin.dsl.module

val commonModule = module {
    factory { ErrorMapper(get()) }
}
