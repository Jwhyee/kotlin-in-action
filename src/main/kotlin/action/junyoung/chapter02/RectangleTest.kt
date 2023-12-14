package action.chapter02

fun main() {
    val rect = Rectangle(100, 200)
//    rect.isSquare = false
    println(rect.isSquare)

    val randRect = createRandomRectangle()
    println(randRect.isSquare)
}