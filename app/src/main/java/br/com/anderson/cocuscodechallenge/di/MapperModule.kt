package br.com.anderson.cocuscodechallenge.di

import br.com.anderson.cocuscodechallenge.mapper.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MapperModule {

    @Singleton
    @Provides
    fun provideUserMapper(): UserMapper  = UserMapper()

    @Singleton
    @Provides
    fun provideChallengeMapper(): ChallengeMapper  = ChallengeMapper()

    @Singleton
    @Provides
    fun provideCompletedChallengeMapper(): CompletedChallengeMapper  = CompletedChallengeMapper()

    @Singleton
    @Provides
    fun providePageCompletedChallengeMapper(completedChallengeMapper: CompletedChallengeMapper): PageCompletedChallengeMapper = PageCompletedChallengeMapper(completedChallengeMapper)

    @Singleton
    @Provides
    fun provideAuthoredChallengeMapper(): AuthoredChallengeMapper = AuthoredChallengeMapper()


    @Singleton
    @Provides
    fun provideDataAuthoredChallengeMapper(authoredChallengeMapper: AuthoredChallengeMapper): DataAuthoredChallengeMapper = DataAuthoredChallengeMapper(authoredChallengeMapper)

}
