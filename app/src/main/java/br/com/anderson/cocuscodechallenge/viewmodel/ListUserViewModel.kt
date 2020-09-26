package br.com.anderson.cocuscodechallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.anderson.cocuscodechallenge.repository.UserRepository
import br.com.anderson.cocuscodechallenge.testing.OpenForTesting
import javax.inject.Inject

@OpenForTesting
class LoginViewModel @Inject constructor(repository: UserRepository) : ViewModel()  {



}