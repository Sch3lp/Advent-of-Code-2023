package be.swsb.aoc2023

import kotlin.math.absoluteValue
import kotlin.math.sign

fun readFile(fileName: String): String =
    {}::class.java.classLoader.getResourceAsStream(fileName)?.reader()?.readText() ?: error("Could not load $fileName")


data class Point(val x: Int, val y: Int) {

    //@formatter:off
    val neighbours: Set<Point>
        get() = listOf(
            Point(-1, -1), Point(0, -1), Point(1, -1),
            Point(-1, 0), /*  this   */  Point(1, 0),
            Point(-1, 1), Point(0, 1), Point(1, 1),
        ).map { vector -> this + vector }
            .toSet()

    val orthogonalNeighbours: Set<Point>
        get() = listOf(
            Point(0, -1),
            Point(-1, 0), Point(1, 0),
            Point(0, 1),
        ).map { vector -> this + vector }
            .toSet()
    //@formatter:on

    val diagonalNeighbours: Set<Point>
        get() = listOf(
            Point(-1, -1), Point(1, -1),
            Point(-1, 1), Point(1, 1),
        ).map { vector -> this + vector }
            .toSet()

    fun isDiagonalTo(other: Point): Boolean = this in other.diagonalNeighbours

    operator fun plus(vector: Point) = Point(x + vector.x, this.y + vector.y)
    operator fun times(multiplier: Int) = Point(x = x * multiplier, y = y * multiplier)
    operator fun rangeTo(other: Point): Set<Point> {
        var cur = this
        val points = mutableSetOf(cur)
        while (cur != other) {
            cur += cur.determineVectorTo(other)
            points.add(cur)
        }
        return points
    }
    infix fun until(other: Point) = (this..other) - other

    private fun determineVectorTo(other: Point): Point {
        return if (this.x == other.x) Point(0, (other.y - this.y).sign)
        else if (this.y == other.y) Point((other.x - this.x).sign, 0)
        else Point((other.x - this.x).sign, (other.y - this.y).sign)
    }

    fun searchRight(pointPredicate: (Point) -> Boolean) : Point? =
        if (pointPredicate(this + Point(1,0))) { this + Point(1,0) }
        else null
}

/** Least common multiple **/
fun List<Int>.lcm(): Int =
    map { it.toLong() }.reduce(::lcm).toInt()

/** Least common multiple **/
fun lcm(a: Long, b: Long) : Long
        = (a safeTimes b) / gcd(a,b)

/** Greatest Common Denominator **/
fun gcd(a: Long, b: Long) : Long =
    if (b == 0L) a.absoluteValue else gcd(b, a % b)

// Thanks https://github.com/Zordid/adventofcode-kotlin-2022/blob/main/src/main/kotlin/utils
infix fun Long.safeTimes(other: Long) = (this * other).also {
    check(other == 0L || it / other == this) { "Long Overflow at $this * $other" }
}

object Debugging {
    private var debugEnabled = false
    fun <T> T.debug(block: (it: T) -> String?) = if (debugEnabled) this.also { block(this)?.let { println("$it") } } else this
    fun enable() {
        debugEnabled = true
    }

    fun disable() {
        debugEnabled = false
    }


    fun <T> withDebugging(enabled: Boolean = true, block: () -> T): T {
        debugEnabled = enabled
        return block().also { disable() }
    }
}