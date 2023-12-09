package be.swsb.aoc2023.day6

fun parse(input: String): Races {
    val (times, distances) = input.lines()
        .map { line -> """\d{1,4}""".toRegex().findAll(line).map { it.value.toLong() } }
    return Races(times.zip(distances).toList())
}

fun parse2(input: String): Races {
    val (time, distance) = input.lines()
        .map { line -> """\d{1,4}""".toRegex().findAll(line).joinToString("") { it.value } }.map { it.toLong() }
    return Races(listOf(time to distance))
}

class Races(val highscores: List<Pair<Time, Distance>>) {
    private val boatSpeedPerHoldTime = 1
    fun raceImprovements(): List<ButtonHoldTime> =
        highscores.map { (time, distance) -> raceImprovement(time, distance) }

    private fun raceImprovement(time: Time, distanceToBeat: Distance): ButtonHoldTime {
        val distances = (0..time).map { holdTime -> (time - holdTime) * (holdTime * boatSpeedPerHoldTime) }
        return distances.reduce { acc, cur -> if (cur > distanceToBeat) acc + 1 else acc }
    }
}
typealias Time = Long
typealias Distance = Long
typealias ButtonHoldTime = Long