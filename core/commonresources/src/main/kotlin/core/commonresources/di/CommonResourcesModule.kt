package core.commonresources.di

import core.commonresources.StringProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val commonResourcesModule = module {
    factory { StringProvider(androidContext()) }
}
