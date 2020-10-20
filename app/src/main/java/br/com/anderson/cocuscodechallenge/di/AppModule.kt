package br.com.anderson.cocuscodechallenge.di

import android.app.Application
import br.com.anderson.cocuscodechallenge.provider.ResourceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, NetworkDataSourceModule::class, LocalDataSourceModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideResource(app: Application): ResourceProvider {
        return ResourceProvider(app)
    }
}
