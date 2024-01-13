package action.junyoung.chapter05.part4

import action.junyoung.chapter05.FunctionalTestJava.*

val runnable = Runnable { println(42) }

fun handleComputation(id: String) {
    postponeComputation(1000) { println(id) }
}

fun main() {
    postponeComputation(1000) { println(42) }

    postponeComputation(1000, runnable)

    handleComputation("42")
}