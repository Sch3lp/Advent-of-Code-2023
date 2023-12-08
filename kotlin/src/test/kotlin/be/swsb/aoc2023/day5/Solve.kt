package be.swsb.aoc2023.day5

import be.swsb.aoc2023.readFile
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

private val exampleInput = "2023/day5/exampleInput.txt"
private val actualInput = "2023/day5/input.txt"

class Solve {

    @Test
    fun `example input part 1`() {
        val input = readFile(exampleInput)
        solve1(input) shouldBeEqual 35
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile(actualInput)
        solve1(input) shouldBeEqual 71124
    }

    @Test
    @Disabled
    fun `example input part 2`() {
    }

    @Test
    @Disabled
    fun `actual input part 2`() {
    }

    @Test
    fun `associate seed with soil`() {
        val almanac = parseToAlmanac(readFile(exampleInput))

        val expected = (0..49).zip(0..49) +
                (50..97).zip(52..99) +
                (98..99).zip(50..51)

        almanac.seedToSoilMap shouldBeEqual expected.associate { (l, r) -> "$l" to "$r" }
    }


    private fun solve1(input: String): Int = 0

}

fun parseToAlmanac(input: String): Almanac {
    val maps = input.split(":").forEach(::println)
    TODO()
}

class Almanac {
    val seedToSoilMap: Map<String, String> = emptyMap()
}