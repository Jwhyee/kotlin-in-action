package action.junyoung.chapter03.part4

fun main() {
    val strings = listOf("first", "second", "fourteenth")
    println(strings.last())

    val numbers = setOf(1, 14, 2)
    println(numbers.max())


    val args = arrayOf("First", "Second", "Third")
    val argList = listOf("args: ", *args)

    println(argList)

    val map = mapOf(1 to "One", 7 to "Seven", 53 to "fifty-three")
    for ((key, value) in map.entries) {
        println("${key} = ${value}")
    }

    val list = listOf(2, 3, 5, 7, 11)
    for ((index, element) in list.withIndex()) {
        println("${index} = ${element}")
    }

    list.forEachIndexed { index, element ->
        println("${index} = ${element}")
    }

}