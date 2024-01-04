package action.junyoung.chapter03.part3.override

import action.chapter03.part3.lastChar3
import java.lang.StringBuilder

fun main() {
    val view: View = Button()
    view.click()
    view.showOff()

    extensionFunctionTest()
}

fun extensionFunctionTest() {
    println("Kotlin".lastChar3)
    val sb = StringBuilder("Kotlin?")
    sb.lastChar = '!'
    println(sb.toString())
    println(sb.lastChar)
}