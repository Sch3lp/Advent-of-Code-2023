package be.swsb.aoc2023.day9

fun generateNextSequenceFrom(sequence: List<Long>): List<Long> =
    sequence.zipWithNext().fold(listOf()) { acc, (m1, m2) -> acc + (m2 - m1) }

fun predict(sequence: List<Long>, backwards : Boolean = false): Long {
    return predictForwards(sequence, backwards)
}

private fun predictForwards(sequence: List<Long>, backwards: Boolean = false): Long {
    var curSequence = if (backwards) sequence.reversed() else sequence
    val lastMeasurements = mutableListOf(curSequence.last())
    while (!curSequence.all { it == 0L }) {
        curSequence = generateNextSequenceFrom(curSequence)
        lastMeasurements += curSequence.last()
    }
    return lastMeasurements.sum()
}