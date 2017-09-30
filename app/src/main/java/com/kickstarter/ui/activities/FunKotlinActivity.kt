package com.kickstarter.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import com.kickstarter.R
import com.kickstarter.viewmodels.FunKotlinActivityViewModel

class FunKotlinActivity : AppCompatActivity() {


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_fun_kotlin)
    val vm = FunKotlinActivityViewModel()
    var view: TextView = findViewById(R.id.fun_kotlin_text_view) as TextView;
    vm.listOutput().subscribe{x -> view.text = x }


    var edittext: EditText = findViewById(R.id.plain_text_input) as EditText
    edittext.setOnKeyListener { v, keyCode, event ->
      when {
        (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) -> {
          vm.textInput(edittext.text.toString())
          true
        }
      }
      false
    }
  }
}
