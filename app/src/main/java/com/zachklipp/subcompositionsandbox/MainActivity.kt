package com.zachklipp.subcompositionsandbox

import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.AndroidView

/**
 * This example demonstrates a crash that happens when adding an Android view to a root composition,
 * where the Android view hosts a subcomposition of the root composition by getting a
 * CompositionReference from the root.
 *
 * To trigger the crash, launch the app, then hit the "Add an android child" button. You should see
 * an error that starts like this:
 * ```
 * java.lang.IllegalStateException: Cannot read while a writer is pending
 *   at androidx.compose.runtime.SlotTable.openReader(SlotTable.kt:1162)
 *   at androidx.compose.runtime.Composer.ambientScopeAt(Composer.kt:2867)
 *   at androidx.compose.runtime.Composer.access$ambientScopeAt(Unknown Source:0)
 *   …
 * ```
 *
 * This bug is reproducable in at least Compose versions 0.1.0-dev17 and 1.0.0-alpha01.
 */
class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent { Host() }
  }
}

@Composable
fun Host() {
  var childCount by remember { mutableStateOf(0) }
  Column {
    Text("This is a Composable (Host).")
    Button(onClick = { childCount++ }) {
      Text("Add an android child.")
    }
    val compRef = compositionReference()
    for (i in 0 until childCount) {
      AndroidView(::IntermediateAndroidView) {
        it.compRef = compRef
      }
    }
  }
}

private class IntermediateAndroidView(context: Context) : LinearLayout(context) {
  var compRef: CompositionReference? = null
    set(value) {
      if (field != value) {
        field = value
        if (value == null) return
        setContent(Recomposer.current(), parentComposition = value) {
          Text("I'm a Compose view nested in an android view!")
        }
      }
    }
}
