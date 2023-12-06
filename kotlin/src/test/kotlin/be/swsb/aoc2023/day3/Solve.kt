package be.swsb.aoc2023.day3

import be.swsb.aoc2023.Point
import be.swsb.aoc2023.readFile
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
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
    fun `parse to schematic`() {
        val schematic: Schematic = readFile(exampleInput).parse()

        schematic.allNumbers().map { it.first } shouldContainExactlyInAnyOrder listOf(
            467,
            35,
            633,
            617,
            592,
            755,
            664,
            598,
            114,
            58
        )
        schematic.allNumbersAdjacentToSymbol().map { it.first } shouldContainExactlyInAnyOrder listOf(
            467,
            35,
            633,
            617,
            592,
            755,
            664,
            598
        )
    }
}

private fun String.parse() =
    Schematic(lines().flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            Point(x, y) to char
        }
    }.toMap())


class Schematic(private val points: Map<Point, Char>) {

    fun allNumbersAdjacentToSymbol(): List<Pair<Int, List<Point>>> =
        allNumbers().filter { (_, points) ->
            points.any { point -> isAdjacentToASymbol(point) }
        }

    private fun isAdjacentToASymbol(point: Point) =
        point.neighbours.any { neighbour -> neighbour in this.points.filterValues { !it.isDigit() && it != '.' }.keys }

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

fun solve(string: String) = string.parse().allNumbersAdjacentToSymbol().sumOf { (n,_) -> n }