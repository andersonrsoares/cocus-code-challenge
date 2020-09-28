package br.com.anderson.cocuscodechallenge.di


import br.com.anderson.cocuscodechallenge.ui.ListCompletedChallengeFragment
import br.com.anderson.cocuscodechallenge.ui.ListUserFragment
import br.com.anderson.cocuscodechallenge.ui.UserDetailFragment
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
}

