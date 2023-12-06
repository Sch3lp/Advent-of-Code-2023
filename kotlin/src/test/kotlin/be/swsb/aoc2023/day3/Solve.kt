package be.swsb.aoc2023.day3

import be.swsb.aoc2023.readFile
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Solve {

    private val exampleInput = "day3/exampleInput.txt"
    private val actualInput = "day3/input.txt"

    @Test
    fun `solve part 1`() {
        solve(readFile(exampleInput)) shouldBe 4361
    }
}

fun solve(string: String) = 0