package action.junyoung.chapter08.part3

private data class Person(val name: String, val age: Int)

private fun lookForAlice(people: List<Person>) {
    people.forEach { person ->
        if (person.name == "Alice") {
            println("Found")
            return
        }
    }
//    for (person in people) {
//        if (person.name == "Alice") {
//            println("Found")
//            return
//        }
//    }
}

fun main() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    lookForAlice(people)
}