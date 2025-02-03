package com.vanish.standard.example

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest : FreeSpec({
    "addition_isCorrect" {
        2 + 2 shouldBe 4
    }
})