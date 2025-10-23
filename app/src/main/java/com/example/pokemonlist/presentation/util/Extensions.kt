package com.example.pokemonlist.presentation.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

fun View.show() {
    isVisible = true
}

fun View.hide() {
    isVisible = false
}

fun View.showIf(condition: Boolean) {
    isVisible = condition
}

fun View.snackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT) =
    Snackbar.make(this, message, duration).show()

@SuppressLint("CheckResult")
fun ImageView.loadImage(
    url: String?,
    placeholder: Int? = null,
    error: Int? = null,
    shouldCenterCrop: Boolean = false,
) = Glide.with(this)
    .load(url)
    .apply {
        placeholder?.let { placeholder(it) }
        error?.let { error(it) }
        if (shouldCenterCrop) centerCrop()
    }
    .into(this)

fun String.capitalize() = this.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase() else it.toString()
}

fun String.extractPokemonId() = this.dropLast(1)
    .substringAfterLast("/")
    .toIntOrNull()

fun Fragment.hideKeyboard() = view?.let { activity?.hideKeyboard(it) }

private fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
