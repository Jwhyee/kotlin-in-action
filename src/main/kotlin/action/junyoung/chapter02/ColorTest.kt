package action.junyoung.chapter02

import action.chapter02.Color.*
import java.lang.IllegalArgumentException

fun main() {
    println(getMnemonic(BLUE))
    println(getWarmth(ORANGE))
    println(mix(BLUE, YELLOW))
}

fun mixOptimized(c1: Color, c2: Color) = when {
    (c1 == RED && c2 == YELLOW) ||
    (c1 == YELLOW && c2 == RED) ->
        ORANGE
    (c1 == YELLOW && c2 == BLUE) ||
    (c1 == BLUE && c2 == YELLOW) ->
        GREEN
    (c1 == BLUE && c2 == VIOLET) ||
    (c1 == VIOLET && c2 == BLUE) ->
        INDIGO
    else -> throw IllegalArgumentException("Dirty color")
}

fun mix(c1: Color, c2: Color) = when (setOf(c1, c2)) {
    setOf(RED, YELLOW) -> ORANGE
    setOf(YELLOW, BLUE) -> GREEN
    setOf(BLUE, VIOLET) -> INDIGO
    else -> throw IllegalArgumentException("Dirty color")
}

fun getWarmth(color: Color) = when (color) {
    RED, ORANGE, YELLOW -> "warm"
    GREEN -> "neutral"
    BLUE, INDIGO, VIOLET -> "cold"
}

fun getMnemonic(color: Color) =
    when (color) {
        RED -> "Rechard"
        ORANGE -> "Of"
        YELLOW -> "York"
        GREEN -> "Gave"
        BLUE -> "Battle"
        INDIGO -> "In"
        VIOLET -> "Vain"
    }