package be.swsb.aoc2023.day3

import be.swsb.aoc2023.Point
import be.swsb.aoc2023.readFile
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Solve {

    private val exampleInput = "2023/day3/exampleInput.txt"
    private val actualInput = "2023/day3/input.txt"

    @Test
    fun `solve part 1`() {
        solve(readFile(exampleInput)) shouldBe 4361
        solve(readFile(actualInput)) shouldBe 553825
    }

    @Test
    fun `solve part 2`() {
        solve2(readFile(exampleInput)) shouldBe 467835L
        solve2(readFile(actualInput)) shouldBeGreaterThan 49062287L
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