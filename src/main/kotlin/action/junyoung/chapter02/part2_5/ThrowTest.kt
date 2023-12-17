package action.junyoung.chapter02.part2_5

fun main() {

}

var number = 100

fun checkPercentage(percentage: Int) {
    if (percentage !in 0..100) {
        throw IllegalArgumentException(
            "A percentage value must be between 0 and 100 : $percentage"
        )
    }
}

val percentage = if (number in 0..100) number else {
    throw IllegalArgumentException(
            "A percentage value must be between 0 and 100 : $number"
    )
}