
package br.com.anderson.cocuscodechallenge.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.anderson.cocuscodechallenge.viewmodel.ListCompletedChallengeViewModel
import br.com.anderson.cocuscodechallenge.viewmodel.ListUserViewModel
import br.com.anderson.cocuscodechallenge.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Singleton
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ListUserViewModel::class)
    abstract fun bindListUserViewModel(viewModel: ListUserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListCompletedChallengeViewModel::class)
    abstract fun bindListCompletedChallengeViewModel(viewModel: ListCompletedChallengeViewModel): ViewModel

}
