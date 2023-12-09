package be.swsb.aoc2023.day9

import be.swsb.aoc2023.readFile
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test

class Solve {
    private val exampleInput = "2023/day9/exampleInput.txt"
    private val actualInput = "2023/day9/input.txt"

    fun parse(input: String) = input.lines().map { line -> line.split(" ").map { it.toLong() } }

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
        actual shouldBeEqual 0
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
}

fun generateNextSequenceFrom(sequence: List<Long>): List<Long> =
    sequence.zipWithNext().fold(listOf()) { acc, (m1, m2) -> acc + (m2 - m1) }

fun predict(sequence: List<Long>): Long {
    val lastMeasurements = mutableListOf(sequence.last())
    var curSequence = sequence
    while (!curSequence.all { it == 0L }) {
        curSequence = generateNextSequenceFrom(curSequence)
        lastMeasurements += curSequence.last()
    }
    return lastMeasurements.sum()
}