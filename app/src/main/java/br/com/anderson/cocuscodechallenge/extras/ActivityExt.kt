package br.com.anderson.cocuscodechallenge.extras

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import timber.log.Timber

fun Activity.hideKeyboard() {
    this.currentFocus?.also {view->
        view.hideKeyboard()
    }
}

fun View.hideKeyboard() {
    try {
        val inputManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(this.windowToken, 0)
    } catch (e: Exception) {
        Timber.e(e)
    }

}

fun Fragment.hideKeyboard() = requireActivity().hideKeyboard()