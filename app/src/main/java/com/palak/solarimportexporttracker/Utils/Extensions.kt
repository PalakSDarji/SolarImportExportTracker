package com.palak.solarimportexporttracker.Utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

inline fun <T> MutableList<T>.mutate(transform: (T) -> T): MutableList<T> {
    return mutateIndexed { _, t -> transform(t) }
}

inline fun <T> MutableList<T>.mutateIndexed(transform: (Int, T) -> T): MutableList<T> {
    val iterator = listIterator()
    var i = 0
    while (iterator.hasNext()) {
        iterator.set(transform(i++, iterator.next()))
    }
    return this
}