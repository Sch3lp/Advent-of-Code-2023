package be.swsb.aoc2023.day3

import be.swsb.aoc2023.Point

fun String.parse() =
    Schematic(lines().flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            Point(x, y) to char
        }
    }.toMap())
typealias Gear = Pair<Point, Set<Int>>

val Gear.ratio get() = this.second.map { it.toLong() }.reduce(Long::times)

class Schematic(private val points: Map<Point, Char>) {

    fun allGears(): List<Gear> {
        val numbersNextToAsterisks = allNumbersAdjacentToSymbol('*').toMap()
        val allAsterisks = points.filterValues { it == '*' }.keys
        return allAsterisks.map { asterisk ->
            asterisk to numbersNextToAsterisks.filterValues { points -> points.any { point -> point in asterisk.neighbours } }.keys
        }.filter { (_,numbers) -> numbers.size == 2 }
    }

    fun allNumbersAdjacentToSymbol(symbol: Char? = null): List<Pair<Int, List<Point>>> =
        allNumbers().filter { (_, points) ->
            points.any { point -> point.isAdjacentToASymbol(symbol) }
        }

    private fun Point.isAdjacentToASymbol(symbol: Char? = null) =
        neighbours.any { neighbour ->
            neighbour in this@Schematic.points.filterValues {
                if (symbol == null) !it.isDigit() && it != '.'
                else it == symbol
            }.keys
        }

    fun allNumbers(): List<Pair<Int, List<Point>>> {
        val maxX = points.maxOf { (k, _) -> k.x }
        val maxY = points.maxOf { (k, _) -> k.y }
        val numbers = mutableListOf<Pair<Int, List<Point>>>()

        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                val point = Point(x, y)
                if (point !in numbers.flatMap { it.second } && points[point]?.isDigit() == true) {
                    var curNumber = "${points[point]}"
                    val curPoints = mutableListOf(point)
                    var curPoint = point
                    while (curPoint.searchRight { p -> points[p]?.isDigit() == true } != null) {
                        val rightNeighbour = curPoint.searchRight { p -> points[p]?.isDigit() == true }
                        curNumber += "${points[rightNeighbour]}"
                        curPoints += rightNeighbour!!
                        curPoint = rightNeighbour!!
                    }
                    numbers += curNumber.toInt() to curPoints
                }
            }
        }
        return numbers
    }
}

fun solve(string: String) = string.parse().allNumbersAdjacentToSymbol().sumOf { (n, _) -> n }
fun solve2(string: String) = string.parse().allGears().sumOf { it.ratio }