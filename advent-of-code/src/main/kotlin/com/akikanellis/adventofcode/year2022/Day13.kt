package com.akikanellis.adventofcode.year2022

object Day13 {
    private val DIVIDER_PACKETS = Pair(packet("[[2]]"), packet("[[6]]"))

    fun sumOfPacketPairIndicesInRightOrder(input: String) = packetPairs(input)
        .withIndex()
        .filter { (_, packetPair) -> rightOrder(packetPair.first, packetPair.second)!! }
        .sumOf { (index, _) -> index + 1 }

    fun distressSignalDecoderKey(input: String) = packetPairs(input)
        .plus(DIVIDER_PACKETS)
        .flatMap { it.toList() }
        .sortedWith { a, b -> if (rightOrder(a, b)!!) -1 else 1 }
        .withIndex()
        .filter { (_, packet) ->
            packet == DIVIDER_PACKETS.first || packet == DIVIDER_PACKETS.second
        }
        .map { (index, _) -> index + 1 }
        .reduce(Int::times)

    private fun packetPairs(input: String) = input
        .split("\n\n")
        .map { packetPair -> packetPair.split("\n") }
        .map { (leftPacketText, rightPacketText) ->
            Pair(
                packet(leftPacketText),
                packet(rightPacketText),
            )
        }

    private fun packet(packetText: String) = packetData(
        remainingPacketText = packetText.toMutableList().also { it.removeAt(0) },
        currentPacketList = mutableListOf(),
    )

    private fun packetData(
        remainingPacketText: MutableList<Char>,
        currentPacketList: MutableList<Any>,
    ): List<Any> {
        val nextPacketPart = remainingPacketText.removeAt(0)

        return if (nextPacketPart == '[') {
            val newList = mutableListOf<Any>()
            currentPacketList.add(newList)

            packetData(remainingPacketText, newList)
            packetData(remainingPacketText, currentPacketList)
        } else if (nextPacketPart.isDigit()) {
            val remainingDigits = remainingPacketText
                .subList(0, remainingPacketText.indexOfFirst { !it.isDigit() })
            val packetInteger = listOf(nextPacketPart)
                .plus(remainingDigits)
                .joinToString("")
                .toInt()
            remainingDigits.clear()

            currentPacketList.add(packetInteger)
            packetData(remainingPacketText, currentPacketList)
        } else if (nextPacketPart == ',') {
            packetData(remainingPacketText, currentPacketList)
        } else if (nextPacketPart == ']') {
            currentPacketList.toList()
        } else {
            error("Unsupported packet part: '$nextPacketPart'")
        }
    }

    private fun rightOrder(leftPacketPart: Any?, rightPacketPart: Any?): Boolean? =
        if (leftPacketPart == null && rightPacketPart == null) {
            null
        } else if (leftPacketPart == null) {
            true
        } else if (rightPacketPart == null) {
            false
        } else if (leftPacketPart is List<*> && rightPacketPart is List<*>) {
            rightOrder(leftPacketPart.firstOrNull(), rightPacketPart.firstOrNull())
                ?: rightOrder(
                    leftPacketPart.drop(1).ifEmpty { null },
                    rightPacketPart.drop(1).ifEmpty { null },
                )
        } else if (leftPacketPart is Int && rightPacketPart is Int) {
            when {
                leftPacketPart < rightPacketPart -> true
                leftPacketPart > rightPacketPart -> false
                else -> null
            }
        } else if (leftPacketPart is Int && rightPacketPart is List<*>) {
            rightOrder(listOf(leftPacketPart), rightPacketPart)
        } else if (leftPacketPart is List<*> && rightPacketPart is Int) {
            rightOrder(leftPacketPart, listOf(rightPacketPart))
        } else {
            error(
                "Unsupported packet parts. Left: '$leftPacketPart', right: '$rightPacketPart'",
            )
        }
}
