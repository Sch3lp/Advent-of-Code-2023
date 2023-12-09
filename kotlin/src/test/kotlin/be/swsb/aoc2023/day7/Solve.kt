package be.swsb.aoc2023.day7

import be.swsb.aoc2023.readFile
import io.kotest.inspectors.forAll
import io.kotest.matchers.equals.shouldBeEqual
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

private val exampleInput = "2023/day7/exampleInput.txt"
private val actualInput = "2023/day7/input.txt"

class Solve {

    @Test
    fun `example input part 1`() {
        val input = readFile(exampleInput)
        solve1(input) shouldBeEqual 6440
    }

    @Test
    fun `actual input part 1`() {
        val input = readFile(actualInput)
        solve1(input) shouldBeEqual 0
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
    fun `parse - turns input into a list of hands with their bids`() {
        val actual = parse(readFile(exampleInput))

        actual shouldBeEqual listOf(
            Hand("32T3K") to 765,
            Hand("T55J5") to 684,
            Hand("KK677") to 28,
            Hand("KTJJT") to 220,
            Hand("QQQJA") to 483,
        )
    }

    @Test
    fun `Hand - has its own type`() {
        listOf(
            "AAAAA" to HandType.FiveOfAKind,
            "AAAAT" to HandType.FourOfAKind,
            "AAATT" to HandType.FullHouse,
            "AAAT9" to HandType.ThreeOfAKind,
            "AA9TT" to HandType.TwoPair,
            "AA98T" to HandType.OnePair,
            "A798T" to HandType.HighCard,
        ).forAll { (hand, expectedType) ->
            Hand(hand).type shouldBeEqual expectedType
        }
    }

    @Test
    fun `Hand - can be compared to another Hand`() {
        val sorted = listOf(
            Hand("KK677") to 1,
            Hand("32T3K") to 1,
            Hand("T55J5") to 1,
            Hand("QQQJA") to 1,
            Hand("KTJJT") to 1,
        ).sortedWith(HandComparator)

        sorted shouldBeEqual listOf(
            Hand("32T3K") to 1,
            Hand("KTJJT") to 1,
            Hand("KK677") to 1,
            Hand("T55J5") to 1,
            Hand("QQQJA") to 1,
        )
    }

    private fun solve1(input: String): Long {
        val cards = parse(input)
        val rankedCards = cards.sortedWith(HandComparator).zip(1..cards.size)
        val winnings = rankedCards.map { (handWithBid, rank) -> rank * handWithBid.second }
        return winnings.sum().toLong()
    }

    private fun solve2(input: String): Long = TODO()

}