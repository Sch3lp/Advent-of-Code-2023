package be.swsb.aoc2023.day3

import be.swsb.aoc2023.Point
import be.swsb.aoc2023.readFile
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Solve {

    private val exampleInput = "day3/exampleInput.txt"
    private val actualInput = "day3/input.txt"

    @Test
    fun `solve part 1`() {
        solve(readFile(exampleInput)) shouldBe 4361
        solve(readFile(actualInput)) shouldBe 553825
    }

    @Test
    fun `solve part 2`() {
        solve2(readFile(exampleInput)) shouldBe 467835
        solve2(readFile(actualInput)) shouldBeGreaterThan 49062287
    }

    @Test
    fun `schematic's allNumbers`() {
        val schematic: Schematic = readFile(exampleInput).parse()

        schematic.allNumbers()
            .map { it.first } shouldContainExactlyInAnyOrder listOf(467, 35, 633, 617, 592, 755, 664, 598, 114, 58)
    }

    @Test
    fun `schematic's allNumbers adjacent to a symbol`() {
        val schematic: Schematic = readFile(exampleInput).parse()
        schematic.allNumbersAdjacentToSymbol()
            .map { it.first } shouldContainExactlyInAnyOrder listOf(467, 35, 633, 617, 592, 755, 664, 598)
    }

    @Test
    fun `schematic's gears`() {
        val schematic: Schematic = readFile(exampleInput).parse()
        val allGears = schematic.allGears()
        allGears shouldContainExactlyInAnyOrder listOf(
            Point(3, 1) to setOf(467,35),
            Point(5, 8) to setOf(755,598),
        )
        allGears.map { it.ratio } shouldContainExactlyInAnyOrder listOf(
            16345,
            451490,
        )
    }
}

private fun String.parse() =
    Schematic(lines().flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            Point(x, y) to char
        }
    }.toMap())

typealias Gear = Pair<Point, Set<Int>>
val Gear.ratio get() = this.second.reduce(Int::times)

class Schematic(private val points: Map<Point, Char>) {

    fun allGears(): List<Gear> {
        val numbersNextToAsterisks = allNumbersAdjacentToSymbol('*').toMap()
        val allAsterisks = points.filterValues { it == '*' }.keys
        return allAsterisks.map { asterisk ->
            asterisk to numbersNextToAsterisks.filterValues { points -> points.any { point -> point in asterisk.neighbours } }.keys
        }.filter { (_,numbers) -> numbers.size >= 2 }
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