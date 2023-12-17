package action.junyoung.chapter02.part2_2

import java.util.Random

class Rectangle(val height: Int, val width: Int) {
    val isSquare: Boolean
//        get() = height == width
        get() {
            return height == width
        }
}

fun createRandomRectangle() : Rectangle {
    val random = Random()
    return Rectangle(random.nextInt(), random.nextInt())
}