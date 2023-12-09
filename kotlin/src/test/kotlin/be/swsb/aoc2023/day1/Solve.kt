package be.swsb.aoc2023.day1

import be.swsb.aoc2023.readFile
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test

class Solve {

    @Test
    fun `example input part 1`() {
        val input = readFile("2023/day1/exampleInput.txt")
        solve1(input) shouldBeEqual 142
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile("2023/day1/input.txt")
        solve1(input) shouldBeEqual 71124
    }

    @Test
    fun `example input part 2`() {
        val input = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
        """.trimIndent()
        solve2(input) shouldBeEqual 281
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile("2023/day1/input.txt")
        solve2(input) shouldBeEqual 204639
    }

    @Test
    fun parseInt() {
        parseInt("1abc2")?.shouldBeEqual(12)
    }

    @Test
    fun parseInt2() {
        parseInt2("two1nine") shouldBeEqual 29
        parseInt2("zoneight234") shouldBeEqual 14
    }
}