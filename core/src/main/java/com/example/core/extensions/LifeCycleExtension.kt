package com.example.core.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <T> Fragment.collectWithLifecycle(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline onError: (Throwable) -> Unit = {},
    crossinline block: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch(CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        onError(throwable)
    }) {
        repeatOnLifecycle(state) {
            flow.collect {
                block(it)
            }
        }
    }
}