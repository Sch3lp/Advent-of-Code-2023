package be.swsb.aoc2023.day9

import be.swsb.aoc2023.readFile
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test

class Solve {
    private val exampleInput = "2023/day9/exampleInput.txt"
    private val actualInput = "2023/day9/input.txt"

    private fun parse(input: String) = input.lines().map { line -> line.split(" ").map { it.toLong() } }

    @Test
    fun `solve - part 1 - example input`() {
        val sequences: List<List<Long>> = parse(readFile(exampleInput))
        val actual = sequences.sumOf { sequence -> predict(sequence) }
        actual shouldBeEqual 114
    }

    @Test
    fun `solve - part 1 - actual`() {
        val sequences: List<List<Long>> = parse(readFile(actualInput))
        val actual = sequences.sumOf { sequence -> predict(sequence) }
        actual shouldBeEqual 1757008019
    }

    @Test
    fun `solve - part 2 - example input`() {
        val sequences: List<List<Long>> = parse(readFile(exampleInput))
        val actual = sequences.sumOf { sequence -> predict(sequence, true) }
        actual shouldBeEqual 2
    }

    @Test
    fun `solve - part 2 - actual`() {
        val sequences: List<List<Long>> = parse(readFile(actualInput))
        val actual = sequences.sumOf { sequence -> predict(sequence, true) }
        actual shouldBeEqual 995
    }

    @Test
    fun `generateNextSequence - generates sequence based on the differences between the numbers`() {
        val sequence1 = listOf(0L, 3L, 6L, 9L, 12L, 15L)

        generateNextSequenceFrom(sequence1) shouldBeEqual listOf(3L, 3L, 3L, 3L, 3L)
    }

    @Test
    fun `predict - uses generated sequences to predict next value`() {
        predict(listOf(0L, 3L, 6L, 9L, 12L, 15L)) shouldBeEqual 18L
        predict(listOf(1L, 3L, 6L, 10L, 15L, 21L)) shouldBeEqual 28L
        predict(listOf(10L, 13L, 16L, 21L, 30L, 45L)) shouldBeEqual 68L
    }

    @Test
    fun `predictBackwards - uses generated sequences to predict next value backwards`() {
        predict(listOf(0L, 3L, 6L, 9L, 12L, 15L), true) shouldBeEqual -3L
        predict(listOf(1L, 3L, 6L, 10L, 15L, 21L), true) shouldBeEqual 0L
        predict(listOf(10L, 13L, 16L, 21L, 30L, 45L), true) shouldBeEqual 5L
    }
}

