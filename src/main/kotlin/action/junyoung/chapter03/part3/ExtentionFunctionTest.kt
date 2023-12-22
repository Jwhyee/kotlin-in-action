package action.chapter03.part3

fun String.lastChar(): Char = this[this.length - 1]
fun String.lastChar2(): Char = get(length - 1)

fun main() {
    val ch = "Hello".lastChar()
    val ch2 = "Hello".lastChar2()
    println(ch)
    println(ch2)
}