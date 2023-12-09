package be.swsb.aoc2023.day8

fun Network.navigateWith(instructions: List<Instruction>): Long {
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