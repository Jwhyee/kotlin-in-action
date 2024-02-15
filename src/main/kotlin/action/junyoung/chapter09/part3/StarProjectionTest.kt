package action.junyoung.chapter09.part3

import java.util.Collections
import java.util.Random
import kotlin.reflect.KClass

fun printFirst(list: List<*>) {
    if (list.isNotEmpty()) {
        println(list.first())
    }
}

private interface FieldValidator<in T> {
    fun validate(input: T): Boolean
}

private object DefaultStringValidator : FieldValidator<String> {
    override fun validate(input: String) = input.isNotEmpty()
}

private object DefaultIntValidator : FieldValidator<Int> {
    override fun validate(input: Int) = input >= 0
}

fun validatorTest() {
    val validators = mutableMapOf<KClass<*>, FieldValidator<*>>()
    validators[String::class] = DefaultStringValidator
    validators[Int::class] = DefaultIntValidator

//    validators[String::class]!!.validate("Hello")

    val stringValidator = validators[String::class] as FieldValidator<String>
    stringValidator.validate("Hello")

}

fun main() {
    val list: MutableList<Any?> = mutableListOf('a', 1, "QWE")
    val chars = mutableListOf('a', 'b', 'c')
    val unknownElements: MutableList<*> = if (Random().nextBoolean()) list else chars

    println(unknownElements)

    validatorTest()

//    unknownElements.add(42)
}

