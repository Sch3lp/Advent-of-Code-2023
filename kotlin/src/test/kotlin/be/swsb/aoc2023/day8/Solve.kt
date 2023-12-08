package be.swsb.aoc2023.day8

import be.swsb.aoc2023.readFile
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

private val exampleInput = "2023/day8/exampleInput.txt"
private val actualInput = "2023/day8/input.txt"

private fun solve1(input: String): Long {
    val (instructions, network) = parse(input)
    return network.navigateWith(instructions)
}

private fun solve2(input: String): Long = TODO()


class Solve {

    @Test
    fun `example input part 1`() {
        val input = readFile(exampleInput)
        solve1(input) shouldBeEqual 2
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile(actualInput)
        solve1(input) shouldBeEqual 17263
    }

    @Test
    @Disabled
    fun `example input part 2`() {
        val input = readFile(exampleInput)
        solve2(input) shouldBeEqual 71503
    }

    @Test
    @Disabled
    fun `actual input part 2`() {
        val input = readFile(actualInput)
        solve2(input) shouldBeEqual 40087680
    }

    @Test
    fun `example input 2 part 1`() {
        val exampleInput2 = """
            LLR

            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent()
        solve1(exampleInput2) shouldBeEqual 6
    }

    @Test
    fun `parse can parse input to an instructionset and a network`() {
        val expected = mapOf(
            "AAA" to ("BBB" to "CCC"),
            "BBB" to ("DDD" to "EEE"),
            "CCC" to ("ZZZ" to "GGG"),
            "DDD" to ("DDD" to "DDD"),
            "EEE" to ("EEE" to "EEE"),
            "GGG" to ("GGG" to "GGG"),
            "ZZZ" to ("ZZZ" to "ZZZ"),
        )

        val (instructions, network) = parse(readFile(exampleInput))
        instructions shouldBeEqual listOf("R", "L")
        network shouldBeEqual expected
    }

    @Test
    fun `navigate - navigates the network according to the instructions until it reaches ZZZ`() {
        val (instructions, network) = parse(readFile(exampleInput))
        val steps = network.navigateWith(instructions)
        steps shouldBeEqual 2
    }

}

private fun Network.navigateWith(instructions: List<Instruction>): Long {
    var steps = 0L
    var node = "AAA"
    while (node != "ZZZ") {
        val instruction = instructions[(steps % instructions.size).toInt()]
        node = this[node]?.let { instruction.follow(it) } ?: error("Could not find $node in network")
        steps++
    }
    return steps
}

fun Instruction.follow(path: Path) = when (this) {
    "L" -> path.first
    "R" -> path.second
    else -> error("unknown instruction: $this")
}

fun parse(input: String): Pair<List<Instruction>, Network> {
    val instructions = input.lines().first().map { "$it" }
    val networkDef = input.lines().drop(2)
    val network = networkDef.map { line ->
        val (node, paths) = line.split(" = ")
        val (l, r) = paths.split(", ").map { it.replace("(", "").replace(")", "") }
        node to (l to r)
    }.toMap()
    return instructions to network
}

typealias Path = Pair<Node, Node>
typealias Node = String
typealias Instruction = String
typealias Network = Map<Node, Pair<Node, Node>>

