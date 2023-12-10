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
    fun `part 2 - anything but the pipe loop can be considered Ground`() {
        val part2Input = """
            ...........
            .S-------7.
            .|F-----7|.
            .||.|7J.||.
            .||..|L7||.
            .|L-7.F-J|.
            .|..|.|..|.
            .L--J.L--J.
            ...........
        """.trimIndent()

        val enhancedPart2Input = """
            ...........
            .S-------7.
            .|F-----7|.
            .||.....||.
            .||.....||.
            .|L-7.F-J|.
            .|..|.|..|.
            .L--J.L--J.
            ...........
        """.trimIndent()

        val originalMap = PipeMap.fromString(part2Input)
        originalMap.enhance() shouldBeEqual PipeMap.fromString(enhancedPart2Input)
    }

    @Test
    fun `part 2 - find contained tiles - first example`() {
        PipeMap.fromString(
            """
                ...........
                .S-------7.
                .|F-----7|.
                .||.....||.
                .||.....||.
                .|L-7.F-J|.
                .|..|.|..|.
                .L--J.L--J.
                ...........
            """.trimIndent()
        ).enhance().containedPoints() shouldBeEqual setOf(at(2, 6), at(3, 6), at(7, 6), at(8, 6))

    }

    @Test
    fun `find contained - pipeline on edge of diagram`() {
        PipeMap.fromString("""
            |JJJJ|
            |S--7|
            ||..||
            |L--J|
            |LLLL|
        """.trimIndent()).enhance().containedPoints() shouldBeEqual setOf(at(2,2),at(3,2))
        PipeMap.fromString("""
            S--7
            |..|
            L--J
        """.trimIndent()).enhance().containedPoints() shouldBeEqual setOf(at(1,1),at(2,1))
    }

    @Test
    fun `part 2 - find contained tiles - second example`() {
        withDebugging {
            PipeMap.fromString(
                """
                    .F----7F7F7F7F-7....
                    .|F--7||||||||FJ....
                    .||.FJ||||||||L7....
                    FJL7L7LJLJ||LJ.L-7..
                    L--J.L7...LJS7F-7L7.
                    ....F-J..F7FJ|L7L7L7
                    ....L7.F7||L7|.L7L7|
                    .....|FJLJ|FJ|F7|.LJ
                    ....FJL-7.||.||||...
                    ....L---J.LJ.LJLJ...
                """.trimIndent()
            ).enhance().containedPoints().size shouldBeEqual 8
        }
    }

    @Test
    fun `part 2 - find contained tiles - last example`() {
        withDebugging {
            PipeMap.fromString(
                """
                    FF7FSF7F7F7F7F7F---7
                    L|LJ||||||||||||F--J
                    FL-7LJLJ||||||LJL-77
                    F--JF--7||LJLJ7F7FJ-
                    L---JF-JLJ.||-FJLJJ7
                    |F|F-JF---7F7-L7L|7|
                    |FFJF7L7F-JF7|JL---7
                    7-L-JL7||F7|L7F-7F7|
                    L.L7LFJ|||||FJL7||LJ
                    L7JLJL-JLJLJL--JLJ.L
                """.trimIndent()
            ).enhance().containedPoints().size shouldBeEqual 10
        }
    }

    @Test
    fun `solve part 1 - example`() {
        solve1(readFile(exampleInput)) shouldBeEqual 8
    }

    @Test
    fun `solve part 1 - actual`() {
        solve1(readFile(actualInput)) shouldBeEqual 6828
    }

    @Test
    fun `solve part 2 - actual`() {
        solve2(readFile(actualInput)) shouldBeEqual 0
    }
}
