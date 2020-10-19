package br.com.anderson.cocuscodechallenge.di

import br.com.anderson.cocuscodechallenge.ui.challenge.ChallengeFragment
import br.com.anderson.cocuscodechallenge.ui.listauthored.ListAuthoredChallengeFragment
import br.com.anderson.cocuscodechallenge.ui.listcompleted.ListCompletedChallengeFragment
import br.com.anderson.cocuscodechallenge.ui.listuser.ListUserFragment
import br.com.anderson.cocuscodechallenge.ui.userdetail.UserDetailFragment
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
