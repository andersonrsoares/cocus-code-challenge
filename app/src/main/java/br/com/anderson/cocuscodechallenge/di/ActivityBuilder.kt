package br.com.anderson.cocuscodechallenge.di

import br.com.anderson.cocuscodechallenge.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainFragmentBuildersModule::class])
    internal abstract fun contributeMainActivity(): MainActivity

}
