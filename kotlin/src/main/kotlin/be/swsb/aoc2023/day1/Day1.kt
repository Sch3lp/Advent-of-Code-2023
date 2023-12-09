package be.swsb.aoc2023.day1

fun solve1(input: String): Int = input.lines().mapNotNull { parseInt(it) }.also { println(it) }.sum()
fun parseInt(it: String) = it.replace(Regex("""\D"""), "").let { "${it.first()}${it.last()}" }.toIntOrNull()
fun parseInt2(it: String): String {
    return it.windowed(5,1).map { part -> digits.first { d -> d in part } }
        .let { matches -> "${digitValues[matches.first()]}" + "${digitValues[matches.last()]}" }
}

val digits = (0..9).map { it.toString() } + listOf("zero","one","two","three","four","five","six","seven","eight","nine")
val digitValues = digits.zip((0..9)+(0..9)).toMap()
fun solve2(input: String): Int = solve1(input)