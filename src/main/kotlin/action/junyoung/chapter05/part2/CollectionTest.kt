package action.junyoung.chapter05.part2

import action.junyoung.chapter05.part1.Person
import action.junyoung.chapter05.part1.joinToString

private val people = listOf(Person("Abraham", 11), Person("John Cinema", 11), Person("Sponge", 97), Person("Bob", 97))

fun main() {
    val canBeInClub27 = {p: Person -> p.age <= 27}
    println(people.all(canBeInClub27))
    println(people.any(canBeInClub27))
    println(people.count(canBeInClub27))

    val map = people.groupBy { it.age }
    println(map)
    println(map.mapValues { it.value.joinToString { p -> p.name } })
}