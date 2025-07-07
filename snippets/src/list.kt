package snippets

data class Point(val x: Int = 0, val y: Int = 0) {
    override fun toString(): String = "($x, $y)"
}

fun main() {
    val points = MutableList<Point?>(2) { null }
    println(points)

    points[0] = Point(1, 2)
    points[1] = Point(3, 4)
    println(points)

    points[0] = null
    println(points)

    var p = points[1]
    if (p != null) {
        println("p: $p")
        p = Point(5,6)
        println("p: $p")
    }
    println(points)
}