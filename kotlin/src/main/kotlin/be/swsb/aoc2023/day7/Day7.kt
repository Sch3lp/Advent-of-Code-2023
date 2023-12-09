package be.swsb.aoc2023.day7

import be.swsb.aoc2023.day7.Card.*

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