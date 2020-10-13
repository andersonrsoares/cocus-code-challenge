package br.com.anderson.cocuscodechallenge.di

import br.com.anderson.cocuscodechallenge.ui.ChallengeFragment
import br.com.anderson.cocuscodechallenge.ui.ListUserFragment
import br.com.anderson.cocuscodechallenge.ui.UserDetailFragment
import br.com.anderson.cocuscodechallenge.ui.ListCompletedChallengeFragment
import br.com.anderson.cocuscodechallenge.ui.ListAuthoredChallengeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

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
