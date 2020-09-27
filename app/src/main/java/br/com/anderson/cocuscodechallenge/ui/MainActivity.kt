package br.com.anderson.cocuscodechallenge.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.anderson.cocuscodechallenge.R
import br.com.anderson.cocuscodechallenge.di.AppInjector
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun androidInjector() = dispatchingAndroidInjector
}