package action.junyoung.chapter07.part1

data class Point(var x: Int, var y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    operator fun plusAssign(other: Point) {
        x += other.x
        y += other.y
    }
}

operator fun Point.times(scale: Double): Point {
    return Point((x * scale).toInt(), (y * scale).toInt())
}

operator fun Point.unaryMinus(): Point {
    return Point(-x, -y)
}

fun main() {
    val p1 = Point(10, 20)
    val p2 = Point(30, 40)
    println(p1 + p2)

    p1 += p2
    println(p1)

    println(p1 * 1.5)

    listTest()
}

fun listTest() {
    val list = arrayListOf(1, 2)
    list += 3
    val newList = list + listOf(4, 5)
    println(list)
    println(newList)
}