package com.kickstarter.viewmodels

import com.kickstarter.libs.rx.transformers.Transformers
import rx.Observable
import rx.subjects.PublishSubject


class FunKotlinActivityViewModel {

  val outputSubject: PublishSubject<List<Int>>
  val inputSubject: PublishSubject<List<Int>>
  val screenOutput: Observable<String>
  val textInput: PublishSubject<String>
  val shouldClearTextInput: Observable<Boolean>

  init {
    this.inputSubject = PublishSubject.create()
    this.outputSubject = PublishSubject.create()
    this.textInput = PublishSubject.create()

    this.shouldClearTextInput = Observable.just(true)
      .compose(Transformers.takeWhen(textInput))

    this.screenOutput = textInput
  }

  fun textInput(stringInput: String) {
    textInput.onNext(stringInput)
  }

  fun listInput(ints: List<Int>) {
    this.inputSubject.onNext(ints)
  }


  fun screenOutput(): Observable<String> {
    return this.screenOutput
  }

  fun shouldClearTextInput(): Observable<Boolean> {
    return this.shouldClearTextInput
  }
}