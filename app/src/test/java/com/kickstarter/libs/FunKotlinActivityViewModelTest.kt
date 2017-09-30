package com.kickstarter.libs

import com.kickstarter.viewmodels.FunKotlinActivityViewModel
import org.junit.Test
import kotlin.test.assertEquals

class FunKotlinActivityViewModelTest {

  @Test fun test_noThrees() {
    var outputAggregator: String = ""
    val vm = FunKotlinActivityViewModel()
    vm.listOutput().subscribe{x -> outputAggregator + x}
    vm.listInput(listOf(1,2,5))
    assertEquals("", outputAggregator)
  }
}