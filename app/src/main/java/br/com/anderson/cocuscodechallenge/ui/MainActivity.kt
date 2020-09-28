package br.com.anderson.cocuscodechallenge.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.anderson.cocuscodechallenge.R
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        val cidades = Observable.create<List<String>> { emitter ->
//            emitter.onNext(listOf(
//                "Tóquio", "Rio", "Berlim", "Denver",
//                "Moscou", "Nairobi", "Helsinque", "Oslo"
//            ))
//            emitter.onComplete()
//        }.subscribeOn(Schedulers.newThread())
//
//        val bb = Observable.create<List<String>> { emitter ->
//            emitter.onNext(listOf("Walt", "Jesse", "Skyler", "Saul", "Hank"))
//            emitter.onComplete()
//        }.subscribeOn(Schedulers.newThread())
//
//
//        Observable.merge(cidades, bb)
//            .subscribe {
//                    t -> println("$t, ")
//            }
    }

    override fun androidInjector() = dispatchingAndroidInjector
}