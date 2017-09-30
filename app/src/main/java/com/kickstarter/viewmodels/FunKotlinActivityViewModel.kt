package com.kickstarter.viewmodels

import rx.Observable
import rx.subjects.PublishSubject


class FunKotlinActivityViewModel() {

  val listOutputObservable: Observable<String>
  val outputSubject: PublishSubject<List<Int>>
  val inputSubject: PublishSubject<List<Int>>
  val threes: Observable<List<Int>>
  val textInput: PublishSubject<String>
  val shouldClearTextInput: Observable<Boolean>

  init {
    this.inputSubject = PublishSubject.create()
    this.outputSubject = PublishSubject.create()
    this.textInput = PublishSubject.create()

    this.shouldClearTextInput = textInput
      .

    this.threes = this.inputSubject
      .map{
        intList -> intList.filter {
          int -> int % 3 == 0
        }
      }

    this.listOutputObservable = this.threes
      .map{
        intList -> intList.fold(""){ accum, int -> accum + int.toString() + ", "}
      }
  }

  fun textInput(stringInput: String) {
    shouldClearTextInput
  }

  fun listInput(ints: List<Int>) {
    this.inputSubject.onNext(ints)
  }

  fun listOutput(): Observable<String> {
    return this.listOutputObservable
  }
}