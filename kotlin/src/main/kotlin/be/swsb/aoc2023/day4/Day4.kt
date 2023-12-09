package be.swsb.aoc2023.day4

fun parse(input: String): List<Card> {
    return input.lines().map { parseLine(it) }
}

fun parseLine(line: String): Card {
    val (card, allNumbers) = line.split(": ")
    val id = card.split(" ").last().toInt()
    val (winning, numbers) = allNumbers.split(" | ").map { it.split(" ").mapNotNull { n -> n.toIntOrNull() } }
    return Card(id, winning, numbers)
}

data class Card(val id: Int, val winning: List<Int>, val numbers: List<Int>) {
    val worth
        get() = numbers.count { it in winning.toSet() }.let {
            if (it == 0) 0
            else (1..it).reduce { acc, _ -> acc * 2 }
        }
}