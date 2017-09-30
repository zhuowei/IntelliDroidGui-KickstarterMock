package com.kickstarter.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.kickstarter.R
import com.kickstarter.viewmodels.FunKotlinActivityViewModel

class FunKotlinActivity : AppCompatActivity() {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_fun_kotlin)
    val vm = FunKotlinActivityViewModel()
    var view: TextView = findViewById(R.id.fun_kotlin_text_view) as TextView

    var editText: EditText = findViewById(R.id.plain_text_input) as EditText
    editText.setOnKeyListener { v, keyCode, event ->
      when {
        (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) -> {
          vm.textInput(editText.text.toString())
          true
        }
      }
      false
    }
    vm.shouldClearTextInput().subscribe { _ -> editText.setText("")}
    vm.screenOutput().subscribe { text -> this.addNewTextView(text) }
  }

  fun addNewTextView(text: String) {
    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    var newTextView: TextView = TextView(this)
    newTextView.setText(text)
    var funKotlinLayout = findViewById(R.id.fun_kotlin_layout) as LinearLayout
    funKotlinLayout.addView(newTextView, params)
  }
}
