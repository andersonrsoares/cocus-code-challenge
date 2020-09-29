package br.com.anderson.cocuscodechallenge.di


import br.com.anderson.cocuscodechallenge.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): ListUserFragment

    @ContributesAndroidInjector
    abstract fun contributeUserDetailFragment(): UserDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeListCompletedChallengeFragment(): ListCompletedChallengeFragment

    @ContributesAndroidInjector
    abstract fun contributeListAuthoredChallengeFragment(): ListAuthoredChallengeFragment

    @ContributesAndroidInjector
    abstract fun contributeChallengeFragment(): ChallengeFragment

}

