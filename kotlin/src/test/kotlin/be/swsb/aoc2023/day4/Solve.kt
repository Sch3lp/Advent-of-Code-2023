package be.swsb.aoc2023.day4

import be.swsb.aoc2023.readFile
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test

class Day4Test {
    private val actualInput = readFile("2023/day4/input.txt")

    @Test
    fun parse() {
        val input = """
           Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
           Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
           Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
           Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
           Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
           Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
       """.trimIndent()

        parse(input).shouldContainExactly(
            Card(id = 1, listOf(41, 48, 83, 86, 17), numbers = listOf(83, 86, 6, 31, 17, 9, 48, 53)),
            Card(id = 2, listOf(13, 32, 20, 16, 61), numbers = listOf(61, 30, 68, 82, 17, 32, 24, 19)),
            Card(id = 3, listOf(1, 21, 53, 59, 44), numbers = listOf(69, 82, 63, 72, 16, 21, 14, 1)),
            Card(id = 4, listOf(41, 92, 73, 84, 69), numbers = listOf(59, 84, 76, 51, 58, 5, 54, 83)),
            Card(id = 5, listOf(87, 83, 26, 28, 32), numbers = listOf(88, 30, 70, 12, 93, 22, 82, 36)),
            Card(id = 6, listOf(31, 18, 13, 56, 72), numbers = listOf(74, 77, 10, 23, 35, 67, 36, 11)),
        )
    }

    @Test
    fun `card worth`() {
        Card(id = 1, listOf(41, 48, 83, 86, 17), numbers = listOf(83, 86, 6, 31, 17, 9, 48, 53)).worth shouldBeEqual 8
        Card(id = 2, listOf(13, 32, 20, 16, 61), numbers = listOf(61, 30, 68, 82, 17, 32, 24, 19)).worth shouldBeEqual 2
        Card(id = 3, listOf(1, 21, 53, 59, 44), numbers = listOf(69, 82, 63, 72, 16, 21, 14, 1)).worth shouldBeEqual 2
        Card(id = 4, listOf(41, 92, 73, 84, 69), numbers = listOf(59, 84, 76, 51, 58, 5, 54, 83)).worth shouldBeEqual 1
        Card(id = 5, listOf(87, 83, 26, 28, 32), numbers = listOf(88, 30, 70, 12, 93, 22, 82, 36)).worth shouldBeEqual 0
        Card(id = 6, listOf(31, 18, 13, 56, 72), numbers = listOf(74, 77, 10, 23, 35, 67, 36, 11)).worth shouldBeEqual 0
    }

    @Test
    fun solve1() {
        println(parse(actualInput).sumOf { it.worth })
    }
}