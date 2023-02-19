package com.akikanellis.adventofcode.year2022

object Day11 {
    fun monkeyBusinessLevel(input: String, rounds: Int, itemDivisor: Int): Long {
        val monkeysWithoutHighWorryLevelItemDivisor = input
            .split("\n\n")
            .map {
                Monkey(
                    monkeyLines = it.lines(),
                    itemDivisor = itemDivisor.toLong()
                )
            }

        val highWorryLevelItemDivisor = monkeysWithoutHighWorryLevelItemDivisor
            .map { it.testDivisor }
            .reduce(Long::times)

        val monkeys = monkeysWithoutHighWorryLevelItemDivisor
            .map { it.copy(highWorryLevelItemDivisor = highWorryLevelItemDivisor) }
            .toMutableList()

        repeat(rounds) {
            for (monkey in monkeys) {
                val monkeyAfterInspectingItems = monkey.inspectItems()
                val monkeyIdToThrownItems = monkeyAfterInspectingItems.monkeyIdToThrownItems()
                val monkeyAfterThrowingAllItems = monkeyAfterInspectingItems.throwAllItems()

                monkeys[monkey.id] = monkeyAfterThrowingAllItems

                for ((monkeyId, thrownItems) in monkeyIdToThrownItems) {
                    monkeys[monkeyId] = monkeys[monkeyId].plusItems(thrownItems)
                }
            }
        }

        return monkeys
            .map { it.numberOfInspections }
            .sortedDescending()
            .take(2)
            .reduce(Long::times)
    }

    private data class Monkey(
        val id: Int,
        val items: List<Long>,
        val operation: Pair<String, String>,
        val testDivisor: Long,
        val receiverIdOnTestPass: Int,
        val receiverIdOnTestFail: Int,
        val itemDivisor: Long,
        val highWorryLevelItemDivisor: Long = itemDivisor,
        val numberOfInspections: Long = 0
    ) {
        constructor(monkeyLines: List<String>, itemDivisor: Long) : this(
            id = monkeyLines[0][7].digitToInt(),
            items = monkeyLines[1]
                .substringAfter(": ")
                .split(", ")
                .map { it.toLong() },
            operation = monkeyLines[2]
                .substringAfter("old ")
                .split(" ")
                .let { Pair(it[0], it[1]) },
            testDivisor = monkeyLines[3]
                .substringAfter("by ")
                .toLong(),
            receiverIdOnTestPass = monkeyLines[4]
                .substringAfter("monkey ")
                .toInt(),
            receiverIdOnTestFail = monkeyLines[5]
                .substringAfter("monkey ")
                .toInt(),
            itemDivisor = itemDivisor
        )

        fun inspectItems() = copy(
            items = items
                .map { operate(it) }
                .map { manageWorryLevel(it) },
            numberOfInspections = numberOfInspections + items.size
        )

        private fun operate(item: Long): Long {
            val other = operation.second.toLongOrNull() ?: item

            return when (operation.first) {
                "+" -> item + other
                "*" -> item * other
                else -> error("Unsupported operation: '${operation.first}'")
            }
        }

        private fun manageWorryLevel(item: Long) =
            if (lowWorryLevelMode()) item / itemDivisor else item % highWorryLevelItemDivisor

        private fun lowWorryLevelMode() = itemDivisor == 3.toLong()

        fun monkeyIdToThrownItems() = items
            .map { item -> Pair(item % testDivisor == 0.toLong(), item) }
            .map { (testPassed, worryLevel) ->
                Pair(
                    if (testPassed) receiverIdOnTestPass else receiverIdOnTestFail,
                    worryLevel
                )
            }
            .groupBy({ (monkeyId, _) -> monkeyId }) { (_, thrownItem) -> thrownItem }

        fun throwAllItems() = copy(items = emptyList())

        fun plusItems(newItems: List<Long>) = copy(items = items + newItems)
    }
}
