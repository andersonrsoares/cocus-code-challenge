package br.com.anderson.cocuscodechallenge.di

import android.app.Application
import androidx.room.Room
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDao
import br.com.anderson.cocuscodechallenge.persistence.CodeWarsDb
import br.com.anderson.cocuscodechallenge.provider.ResourceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalDataSourceModule {

    @Singleton
    @Provides
    fun provideCodeWarsDb(app: Application): CodeWarsDb {
        return Room
            .databaseBuilder(app, CodeWarsDb::class.java, "codewars.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideCodeWarsDao(db: CodeWarsDb): CodeWarsDao {
        return db.codeWarsDao()
    }
}
