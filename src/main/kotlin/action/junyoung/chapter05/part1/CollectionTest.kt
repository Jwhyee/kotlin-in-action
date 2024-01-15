package action.junyoung.chapter05.part1

fun printMessageWithPrefix(messages: Collection<String>, prefix: String) {
    messages.forEach { msg ->
        println("$prefix $msg")
    }
}

fun printProblemCounts(response: Collection<String>) {
    var clientErrors = 0
    var serverErrors = 0
    response.forEach { res ->
        if(res.startsWith("4")) clientErrors++
        else if(res.startsWith("5")) serverErrors++
    }
    println("Client Errors : $clientErrors, Server Errors : $serverErrors")
}

private val people = listOf(Person("Alice", 29), Person("Sponge", 52), Person("Bob", 97))

fun main() {
    val errors = listOf("403 Forbidden", "404 Not Found")
    printMessageWithPrefix(errors, "Error :")

    println(people.filter { it.age >= 30 })
    println(people.map { it.name })

    println(people.filter { it.age >= 30 }.map { it.name })

    println(people.filter { it.age == people.maxBy { p-> p.age }.age })

    val maxAge = people.maxBy { p-> p.age }.age
    println(people.filter { it.age == maxAge })
}