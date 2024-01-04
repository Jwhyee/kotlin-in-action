@file:JvmName("StringFunctions")

package action.chapter03.part1

import java.lang.StringBuilder

/*@JvmOverloads
fun <T> joinToString(
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for ((idx, element) in collection.withIndex()) {
        if (idx > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}*/

fun <T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for ((idx, element) in this.withIndex()) {
        if (idx > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

fun Collection<String>.join(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
) = joinToString(separator, prefix, postfix)

fun main() {
    val set = hashSetOf(1, 7, 53)
    val list = arrayListOf(1, 7, 53)
    val map = hashMapOf(1 to "one", 7 to "seven", 53 to "fifty-three")

    println(set.javaClass)
    println(list.javaClass)
    println(map.javaClass)

    println(list)

    println("list.joinToStirng = ${list.joinToString(", ")}")
    val strList = arrayListOf("One", "Two", "Eight")
    println("join =  ${strList.join()}")

//    println(joinToString(list, separator = " - ", prefix = "{", postfix = "}"))
//    println(joinToString(list, prefix = "{", postfix = "}"))
//    UtilClass.joinToString(list, " - ", "{", "}")
//    println(joinToString(list))
}