package action.chapter02

fun main(args: Array<String>) {
    val person = Person("Bob", true)
    println(person.name)
    println(person.isMarried)
}

fun stringTest(args: Array<String>) {
    val name = if (args.size > 0) args[0] else "Kotlin"
    println("Hello, ${name.uppercase()}")
    println("Hello, ${name}님 반가워요")
}

fun functionTest() {
    println(max(1, 2))
}

fun variableTest() {
    val question = "삶, 우주, 그리고 모든 것에 대한 궁극적인 질문"
    val answer = 42
//    val answer: Int = 42
    val yearsToCompute = 7.5e6
    println(yearsToCompute::class.simpleName)
    val message: String
    if(answer == 42) {
        message = "Success"
    } else {
        message = "Failed"
    }

    val languages = arrayListOf("Java", "Kotlin", "Type Script")
    languages.add("C++")
    println(languages)
}

fun max(a: Int, b: Int) = if (a > b) a else b