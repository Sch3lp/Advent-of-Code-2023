package be.swsb.aoc2023.day10

import be.swsb.aoc2023.Debugging.withDebugging
import be.swsb.aoc2023.Point.Companion.at
import be.swsb.aoc2023.readFile
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Test

class Solve {
    private val exampleInput = "2023/day10/exampleInput.txt"
    private val actualInput = "2023/day10/input.txt"

    @Test
    fun `parse into a pipe map`() {
        val input = """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
        """.trimIndent()

        PipeMap.fromString(input) shouldBeEqual PipeMap(
            listOf(
                at(0, 0) to Ground, at(1, 0) to Ground, at(2, 0) to Ground, at(3, 0) to Ground, at(4, 0) to Ground,
                at(0, 1) to Ground, at(1, 1) to Start, at(2, 1) to HoPipe, at(3, 1) to SWBend, at(4, 1) to Ground,
                at(0, 2) to Ground, at(1, 2) to VePipe, at(2, 2) to Ground, at(3, 2) to VePipe, at(4, 2) to Ground,
                at(0, 3) to Ground, at(1, 3) to NEBend, at(2, 3) to HoPipe, at(3, 3) to NWBend, at(4, 3) to Ground,
                at(0, 4) to Ground, at(1, 4) to Ground, at(2, 4) to Ground, at(3, 4) to Ground, at(4, 4) to Ground,
            ).map { TilePoint(it.first, it.second) }.associateBy(TilePoint::point)
        )
    }

    @Test
    fun `following the pipe loop counts total segments, including the start`() {
        val input = """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
        """.trimIndent()
        withDebugging {
            PipeMap.fromString(input).countSegments() shouldBeEqual 8
        }
    }

    @Test
    fun `following L from (1,3) after (1,2) should lead to (2,3)`() {
        val prev = TilePoint(at(1, 2), Tile.of('|'))
        val cur = TilePoint(at(1, 3), Tile.of('L'))
        val (_, actual) = cur.follow(prev)

        actual shouldBeEqual at(2, 3)
    }

    @Test
    fun `solve part 1 - example`() {
        solve1(readFile(exampleInput)) shouldBeEqual 8
    }

    @Test
    fun `solve part 1 - actual`() {
        solve1(readFile(actualInput)) shouldBeEqual 6828
    }
}
