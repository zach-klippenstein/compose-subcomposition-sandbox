package com.zachklipp.subcompositionsandbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Change the view created here.
    setContentView(CrashyView(this))
  }
}
