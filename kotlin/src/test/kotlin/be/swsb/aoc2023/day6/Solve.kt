package be.swsb.aoc2023.day6

import be.swsb.aoc2023.readFile
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test

private val exampleInput = "2023/day6/exampleInput.txt"
private val actualInput = "2023/day6/input.txt"

class Solve {

    @Test
    fun `example input part 1`() {
        val input = readFile(exampleInput)
        solve1(input) shouldBeEqual 4 * 8 * 9
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile(actualInput)
        solve1(input) shouldBeEqual 1731600
    }

    @Test
    fun `example input part 2`() {
        val input = readFile(exampleInput)
        solve2(input) shouldBeEqual 71503
    }

    @Test
    fun `actual input part 2`() {
        val input = readFile(actualInput)
        solve2(input) shouldBeEqual 40087680
    }

    @Test
    fun `Parse to Race`() {
        val races: Races = parse(readFile(exampleInput))

        races.highscores[0] shouldBeEqual 7 to 9
        races.highscores[1] shouldBeEqual 15 to 40
        races.highscores[2] shouldBeEqual 30 to 200
    }

    @Test
    fun `Button Hold Times`() {
        val races: Races = parse(readFile(exampleInput))

        val holdTimesThatBeatHighscore = races.raceImprovements()
        holdTimesThatBeatHighscore[0] shouldBeEqual 4
        holdTimesThatBeatHighscore[1] shouldBeEqual 8
        holdTimesThatBeatHighscore[2] shouldBeEqual 9
    }

    @Test
    fun `Parse to Race 2`() {
        val races: Races = parse2(readFile(exampleInput))

        races.highscores[0] shouldBeEqual 71530 to 940200
    }

    @Test
    fun `Button Hold Time 2`() {
        val races: Races = parse2(readFile(exampleInput))

        races.raceImprovements().shouldContainExactly(71503)
    }

    private fun solve1(input: String): Long = parse(input).raceImprovements().reduce(Long::times)
    private fun solve2(input: String): Long = parse2(input).raceImprovements().reduce(Long::times)

}