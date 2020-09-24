package br.com.anderson.cocuscodechallenge.di

import br.com.anderson.cocuscodechallenge.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector
    internal abstract fun contributeMainActivity(): MainActivity

}
