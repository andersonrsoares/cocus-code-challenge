package br.com.anderson.cocuscodechallenge.di


import br.com.anderson.cocuscodechallenge.ui.ListUserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): ListUserFragment
}

