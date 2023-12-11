package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.year2022.Day02.RoundResult.DRAW
import com.akikanellis.adventofcode.year2022.Day02.RoundResult.LOSE
import com.akikanellis.adventofcode.year2022.Day02.RoundResult.WIN

object Day02 {
    fun totalScoreForHands(input: String) = firstColumnToSecondColumn(input)
        .map { Pair(Hand.of(it.first), Hand.of(it.second)) }
        .sumOf { (firstHand, secondHand) ->
            secondHand.score + secondHand.roundResultAgainst(firstHand).score
        }

    fun totalScoreForFirstHandAndRoundResult(input: String) = firstColumnToSecondColumn(input)
        .map { Pair(Hand.of(it.first), RoundResult.of(it.second)) }
        .sumOf { (firstHand, roundResult) ->
            roundResult.score + roundResult.handNeededForRoundResult(firstHand).score
        }

    private fun firstColumnToSecondColumn(input: String) = input
        .lines()
        .filter { it.isNotEmpty() }
        .map { it.split(" ") }
        .map { Pair(it[0], it[1]) }

    private enum class Hand(
        val score: Int,
        val firstRepresentation: String,
        val secondRepresentation: String,
    ) {
        ROCK(1, "A", "X") {
            override fun losesAgainst() = PAPER
        },
        PAPER(2, "B", "Y") {
            override fun losesAgainst() = SCISSORS
        },
        SCISSORS(3, "C", "Z") {
            override fun losesAgainst() = ROCK
        },
        ;

        companion object {
            fun of(representation: String) = entries
                .firstOrNull { it.firstRepresentation == representation }
                ?: entries.first { it.secondRepresentation == representation }
        }

        abstract fun losesAgainst(): Hand
        private fun losesAgainst(opponentsHand: Hand) = losesAgainst() == opponentsHand

        fun drawsAgainst() = this
        private fun drawsAgainst(opponentsHand: Hand) = drawsAgainst() == opponentsHand

        fun winsAgainst() = entries.single { it.losesAgainst(this) }

        fun roundResultAgainst(opponentsHand: Hand) = when {
            losesAgainst(opponentsHand) -> LOSE
            drawsAgainst(opponentsHand) -> DRAW
            else -> WIN
        }
    }

    private enum class RoundResult(val score: Int, val representation: String) {
        LOSE(0, "X") {
            override fun handNeededForRoundResult(opponentsHand: Hand) = opponentsHand.winsAgainst()
        },
        DRAW(3, "Y") {
            override fun handNeededForRoundResult(opponentsHand: Hand) =
                opponentsHand.drawsAgainst()
        },
        WIN(6, "Z") {
            override fun handNeededForRoundResult(opponentsHand: Hand) =
                opponentsHand.losesAgainst()
        },
        ;

        companion object {
            fun of(representation: String) = entries.first { it.representation == representation }
        }

        abstract fun handNeededForRoundResult(opponentsHand: Hand): Hand
    }
}
