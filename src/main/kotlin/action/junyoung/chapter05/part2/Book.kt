package action.junyoung.chapter05.part2

class Book(val title: String, val authors: List<String>)

private val books = listOf(Book("kotlin-in-action", listOf("존시나", "다오")), Book("effective java 3/E", listOf("우영미", "다오", "배찌")))
private val stringLists = listOf(listOf("1", "2", "3"), listOf("4", "5", "6"))

fun main() {
    val set = books.flatMap { it.authors }.toSet()
    val flat = stringLists.flatten()
    println(set)
    println(flat)
}