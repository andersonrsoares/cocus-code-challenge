package br.com.anderson.cocuscodechallenge

import android.app.Application
import br.com.anderson.cocuscodechallenge.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject
import  dagger.android.HasAndroidInjector

class ChallengeApp : Application(), HasAndroidInjector {

    @Inject lateinit var dispatchingAndroidInjector : DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

}





