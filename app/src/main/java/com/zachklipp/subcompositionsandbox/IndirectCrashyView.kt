package com.zachklipp.subcompositionsandbox

import android.content.Context
import android.widget.FrameLayout
import androidx.compose.Recomposer
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.material.Button

class FancyCrashyView(context: Context) : FrameLayout(context) {

    init {
        val presenter = Presenter()
        val child = ChildView(context)
        addView(child)
        child.setPresenter(presenter)
    }
}

private class ChildView(context: Context) : FrameLayout(context) {

    fun setPresenter(presenter: Presenter) {
        // Change this method reference to happyUpdate to see expected behavior.
        presenter.viewModelListener = ::crashyUpdate
    }

    /**
     * This update method passes a lambda to [Button]s onClick parameter which calls
     * [ViewModel.onClick].
     */
    private fun crashyUpdate(viewModel: ViewModel) {
        setContent(Recomposer.current()) {
            // However, if this onClick lambda is changed to simply refer to viewModel.onClick, it
            // doesn't crash.
            Button(onClick = { viewModel.onClick() }) {
                Text(text = "click me: ${viewModel.counter}")
            }
        }
    }

    /**
     * This update method passes the function [ViewModel.onClick] directly to [Button]'s onClick
     * parameter.
     */
    private fun happyUpdate(viewModel: ViewModel) {
        setContent(Recomposer.current()) {
            Button(onClick = viewModel.onClick) {
                Text(text = "click me: ${viewModel.counter}")
            }
        }
    }
}


private class ViewModel(
    val counter: Int,
    val onClick: () -> Unit
)

private class Presenter {
    private var counter = 0

    var viewModelListener: (ViewModel) -> Unit = {}
        set(value) {
            field = value
            emitViewModel()
        }

    private fun incrementCounter() {
        counter++
        emitViewModel()
    }

    private fun emitViewModel() {
        viewModelListener(ViewModel(counter, ::incrementCounter))
    }
}
