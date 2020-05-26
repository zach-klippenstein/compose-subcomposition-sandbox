package com.zachklipp.subcompositionsandbox

import android.content.Context
import android.widget.FrameLayout
import androidx.compose.Recomposer
import androidx.compose.escapeCompose
import androidx.ui.core.setContent
import androidx.ui.foundation.Text
import androidx.ui.material.Button

class HappyView(context: Context) : FrameLayout(context) {

    private var counter = 0

    init {
        update()
    }

    /**
     * This version of [update] has no parameters – it stores the counter value as a property on the
     * view. It does not crash, but correctly shows the incrementing counter on the button when
     * clicked.
     */
    private fun update() {
        setContent(Recomposer.current()) {
            Button(onClick = {
                counter++
                update()
            }) {
                Text(text = "click me: $counter")
            }
        }
    }
}
