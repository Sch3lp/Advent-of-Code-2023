package be.swsb.aoc2023.day7

import be.swsb.aoc2023.day7.Card.*
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

object HandComparator : Comparator<Pair<Hand, Bid>> {
    override fun compare(hand1: Pair<Hand, Bid>, hand2: Pair<Hand, Bid>): Int {
        val typeComparison = hand1.first.type.ordinal.compareTo(hand2.first.type.ordinal)
        return if (typeComparison == 0) {
            compareCardsToTheirRespectivePositions(hand1, hand2)
        } else typeComparison
    }

    private fun compareCardsToTheirRespectivePositions(hand1: Pair<Hand, Bid>, hand2: Pair<Hand, Bid>): Int {
        var index = 0
        var handComparison = hand1.first.cards[index].ordinal.compareTo(hand2.first.cards[index].ordinal) * -1
        while (handComparison == 0 && index < 5) {
            index++
            handComparison = hand1.first.cards[index].ordinal.compareTo(hand2.first.cards[index].ordinal) * -1
        }
        return handComparison
    }
}

typealias Bid = Int

enum class HandType {
    HighCard,
    OnePair,
    TwoPair,
    ThreeOfAKind,
    FullHouse,
    FourOfAKind,
    FiveOfAKind,
}

data class Hand(val cards: List<Card>) {
    val type: HandType = cards.determineType()
    private fun List<Card>.determineType(): HandType {
        val amountsPerCard = this.groupBy { it }.mapValues { (_, v) -> v.count() }
        return when {
            amountsPerCard.size == 1 -> HandType.FiveOfAKind
            amountsPerCard.any { (_, amount) -> amount == 4 } -> HandType.FourOfAKind
            amountsPerCard.any { (_, amount) -> amount == 3 } && amountsPerCard.any { (_, amount) -> amount == 2 } -> HandType.FullHouse
            amountsPerCard.any { (_, amount) -> amount == 3 } -> HandType.ThreeOfAKind
            amountsPerCard.filterValues { amount -> amount == 2 }.size == 2 -> HandType.TwoPair
            amountsPerCard.filterValues { amount -> amount == 2 }.size == 1 -> HandType.OnePair
            else -> HandType.HighCard
        }
    }
}

fun Hand(cards: String): Hand = Hand(cards.map { Card(it) })
enum class Card {
    A, K, Q, J, T, `9`, `8`, `7`, `6`, `5`, `4`, `3`, `2`
}

fun parse(string: String): List<Pair<Hand, Int>> =
    string.lines().map { line -> line.split(" ").let { (first, second) -> first.toHand() to second.toInt() } }

private fun String.toHand() = Hand(this)
fun parse2(string: String): Int = 0

fun Card(c: Char) =
    Card.valueOf("$c")

fun Card0(c: Char): Card = when (c) {
    'A' -> A
    'K' -> K
    'Q' -> Q
    'J' -> J
    'T' -> T
    '9' -> `9`
    '8' -> `8`
    '7' -> `7`
    '6' -> `6`
    '5' -> `5`
    '4' -> `4`
    '3' -> `3`
    '2' -> `2`
    else -> error("Could not parse $c into a Card")
}

fun Card2(c: Char) =
    listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2').zip(Card.values().toList())
        .toMap()[c]
        ?: error("Could not parse $c into a Card")

fun Card3(c: Char) =
    values().associateBy { it.name }["$c"] ?: error("Could not parse $c into a Card")
