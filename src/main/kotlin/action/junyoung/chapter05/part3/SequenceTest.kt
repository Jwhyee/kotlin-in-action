package action.junyoung.chapter05.part3

import action.junyoung.chapter05.part1.Person
import java.io.File

private val people = listOf(Person("Abraham", 11), Person("John Cinema", 11), Person("Sponge", 97), Person("Bob", 97))

fun main() {
    val basicList = people
        .map(Person::name)
        .filter { it.startsWith("A") }

    val sequenceList = people.asSequence()
        .map(Person::name)
        .filter { it.startsWith("A") }
        .toList()

    for (s in basicList) {
        println(s)
    }

    println(sequenceList.size)

    listOf(1, 2, 3, 4).asSequence()
        .map { print("i : map($it) "); it * it }
        .filter { print("i : filter($it) "); it % 2 == 0 }

//    println(intermediate.forEach { it })

    listOf(1, 2, 3, 4).asSequence()
        .map { print("t : map($it) "); it * it }
        .filter { print("t : filter($it) "); it % 2 == 0 }
        .toList()

//    println(terminal)

    println()
    println("result = ${listOf(1, 2, 3, 4).asSequence()
        .map { it * it }.find { it > 3 }}"
    )

    val naturalNumbers = generateSequence(0) { it + 1 }
    val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }
    println(numbersTo100.sum())

    val file = File("/.idea/.gitignore")
    println(file.isInsideHiddenDirectory())
}

fun File.isInsideHiddenDirectory() =
    generateSequence(this) { it.parentFile }.any { it.isHidden }