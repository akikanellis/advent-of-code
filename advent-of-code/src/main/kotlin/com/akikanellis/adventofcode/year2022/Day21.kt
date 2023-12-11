package com.akikanellis.adventofcode.year2022

import com.akikanellis.adventofcode.year2022.Day21.Operator.ADDITION
import com.akikanellis.adventofcode.year2022.Day21.Operator.DIVISION
import com.akikanellis.adventofcode.year2022.Day21.Operator.MULTIPLICATION
import com.akikanellis.adventofcode.year2022.Day21.Operator.SUBTRACTION

object Day21 {
    private val MONKEY_REGEX = """(.+): (.+)""".toRegex()

    fun monkeyNumber(input: String, monkeyName: String): Long {
        var monkeys = monkeys(input)

        if (monkeyName != "root") monkeys = solvingForGivenMonkey(monkeys, monkeyName)

        return monkeys[monkeyName]!!.yellNumber(monkeys)
    }

    private fun monkeys(input: String) = input.lines()
        .filter { it.isNotBlank() }
        .map { line ->
            val groupValues = MONKEY_REGEX.matchEntire(line)!!.groupValues
            Monkey.of(name = groupValues[1], job = groupValues[2])
        }.associateBy { it.name }

    private fun solvingForGivenMonkey(
        originalMonkeys: Map<String, Monkey>,
        monkeyName: String,
    ): Map<String, Monkey> {
        val originalRootMonkey = originalMonkeys["root"]!!
        val monkeys = originalMonkeys -
            monkeyName +
            ("root" to originalRootMonkey.withSubtractionOperation())

        val originalMonkeysNotVisited = monkeys.toMutableMap()
        val newMonkeys = mutableMapOf<String, Monkey>()
        val monkeysToVisit = mutableListOf(monkeyName)

        while (monkeysToVisit.isNotEmpty()) {
            val originalMonkeyName = monkeysToVisit.removeFirst()
            val originalMonkey = originalMonkeysNotVisited.remove(originalMonkeyName)

            if (originalMonkeyName == "root") {
                newMonkeys["root"] = NumberMonkey(name = "root", number = 0)
                continue
            }

            if (originalMonkey != null) {
                newMonkeys[originalMonkeyName] = originalMonkey
                monkeysToVisit += originalMonkey.operands

                continue
            }

            val monkeyWithJobContainingOriginalMonkey = originalMonkeysNotVisited.values.single {
                it.jobContainsMonkey(originalMonkeyName)
            }
            val monkeySolvingForOriginalMonkey =
                monkeyWithJobContainingOriginalMonkey.solvingFor(originalMonkeyName)

            originalMonkeysNotVisited.remove(monkeyWithJobContainingOriginalMonkey.name)
            newMonkeys[monkeySolvingForOriginalMonkey.name] = monkeySolvingForOriginalMonkey

            monkeysToVisit += monkeySolvingForOriginalMonkey.operands
        }

        return newMonkeys.toMap()
    }

    private interface Monkey {
        val name: String
        val operands: List<String>

        fun jobContainsMonkey(monkeyName: String) = operands.contains(monkeyName)

        fun withSubtractionOperation(): MathMonkey
        fun yellNumber(monkeys: Map<String, Monkey>): Long
        fun solvingFor(operand: String): MathMonkey

        companion object {
            fun of(name: String, job: String) = if (job.toLongOrNull() != null) {
                NumberMonkey(
                    name = name,
                    number = job.toLong(),
                )
            } else {
                val jobParts = job.split(" ")
                MathMonkey(
                    name = name,
                    firstOperand = jobParts[0],
                    operator = Operator.of(jobParts[1].toCharArray().single()),
                    secondOperand = jobParts[2],
                )
            }
        }
    }

    private data class NumberMonkey(
        override val name: String,
        override val operands: List<String> = emptyList(),
        val number: Long,
    ) : Monkey {
        override fun withSubtractionOperation() = error("Operations are not applicable")
        override fun yellNumber(monkeys: Map<String, Monkey>) = number
        override fun solvingFor(operand: String) = error("Cannot solve for different operands")
    }

    private data class MathMonkey(
        override val name: String,
        val firstOperand: String,
        val operator: Operator,
        val secondOperand: String,
        override val operands: List<String> = listOf(firstOperand, secondOperand),
    ) : Monkey {
        override fun withSubtractionOperation() = copy(operator = SUBTRACTION)

        override fun yellNumber(monkeys: Map<String, Monkey>): Long {
            val firstOperandNumber = operandNumber(firstOperand, monkeys)
            val secondOperandNumber = operandNumber(secondOperand, monkeys)

            return when (operator) {
                ADDITION -> firstOperandNumber + secondOperandNumber
                SUBTRACTION -> firstOperandNumber - secondOperandNumber
                MULTIPLICATION -> firstOperandNumber * secondOperandNumber
                DIVISION -> firstOperandNumber / secondOperandNumber
            }
        }

        private fun operandNumber(operand: String, monkeys: Map<String, Monkey>) =
            operand.toLongOrNull() ?: monkeys[operand]!!.yellNumber(monkeys)

        override fun solvingFor(operand: String) = when (operand) {
            name -> this
            firstOperand -> when (operator) {
                ADDITION -> MathMonkey(
                    name = firstOperand,
                    firstOperand = name,
                    operator = SUBTRACTION,
                    secondOperand = secondOperand,
                )

                SUBTRACTION -> MathMonkey(
                    name = firstOperand,
                    firstOperand = name,
                    operator = ADDITION,
                    secondOperand = secondOperand,
                )

                MULTIPLICATION -> MathMonkey(
                    name = firstOperand,
                    firstOperand = name,
                    operator = DIVISION,
                    secondOperand = secondOperand,
                )

                DIVISION -> MathMonkey(
                    name = firstOperand,
                    firstOperand = name,
                    operator = MULTIPLICATION,
                    secondOperand = secondOperand,
                )
            }

            secondOperand -> when (operator) {
                ADDITION -> MathMonkey(
                    name = secondOperand,
                    firstOperand = name,
                    operator = SUBTRACTION,
                    secondOperand = firstOperand,
                )

                SUBTRACTION -> MathMonkey(
                    name = secondOperand,
                    firstOperand = firstOperand,
                    operator = SUBTRACTION,
                    secondOperand = name,
                )

                MULTIPLICATION -> MathMonkey(
                    name = secondOperand,
                    firstOperand = name,
                    operator = DIVISION,
                    secondOperand = firstOperand,
                )

                DIVISION -> MathMonkey(
                    name = secondOperand,
                    firstOperand = firstOperand,
                    operator = DIVISION,
                    secondOperand = name,
                )
            }

            else -> error("Unknown operand '$operand'")
        }
    }

    private enum class Operator {
        ADDITION,
        SUBTRACTION,
        MULTIPLICATION,
        DIVISION,
        ;

        companion object {
            fun of(representation: Char) = when (representation) {
                '+' -> ADDITION
                '-' -> SUBTRACTION
                '*' -> MULTIPLICATION
                '/' -> DIVISION
                else -> error("Unknown operation '$representation'")
            }
        }
    }
}
