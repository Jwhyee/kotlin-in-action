package action.junyoung.chapter02.part2_4

fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
fun isNotDigit(c: Char) = c !in '0'..'9'

fun recognize(c: Char) = when (c) {
    in '0'..'9' -> "It's a digit!"
    in 'a'..'z', in 'A'..'Z' -> "It's a letter!"
    else -> "IDK"
}

fun main() {
    println(isLetter('q'))
    println(isNotDigit('x'))

    println(recognize('가'))
    println(recognize('8'))

    println("ab" in "ac".."zz")
    println("aaaa" in "aaa".."zzz")
    println("Kotlin" in "Java".."Scala")
    println("Kotlin" in setOf("Java", "Scala"))
}