package action.junyoung.chapter04.part2

class LengthCounter {
    var counter: Int = 0
        private set

    fun addWord(word: String) {
        counter += word.length
    }
}

fun main() {
    val cnt = LengthCounter()
}