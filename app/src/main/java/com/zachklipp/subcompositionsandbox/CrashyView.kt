package com.zachklipp.subcompositionsandbox

import android.content.Context
import android.widget.FrameLayout
import androidx.compose.Recomposer
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.material.Button

class CrashyView(context: Context) : FrameLayout(context) {

    init {
        update(0)
    }

    /**
     * This version of [update] takes one parameter: the value of the counter to display on the
     * button. When clicked, it calls update with an incremented counter value, and crashes.
     */
    private fun update(counter: Int) {
        setContent(Recomposer.current()) {
            Button(onClick = {
                update(counter + 1)
            }) {
                Text(text = "click me: $counter")
            }
        }
    }
}
