package com.akikanellis.adventofcode.year2022.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class CircularListIteratorTest {
    private val emptyListIterator = listOf<Int>().circularListIterator()

    @Test
    fun `does not have next or previous on empty list`() {
        assertFalse(emptyListIterator.hasNext())
        assertFalse(emptyListIterator.hasPrevious())
    }

    @Test
    fun `next and previous indices are always 0 and -1 on empty list`() {
        assertEquals(0, emptyListIterator.nextIndex())
        assertEquals(-1, emptyListIterator.previousIndex())
    }

    @Test
    fun `throws exception when next is retrieved on empty list`() {
        assertThrows(NoSuchElementException::class.java) { emptyListIterator.next() }
    }

    @Test
    fun `throws exception when previous is retrieved on empty list`() {
        assertThrows(NoSuchElementException::class.java) { emptyListIterator.previous() }
    }

    @ParameterizedTest
    @MethodSource("listsWithExpectedNextElements")
    fun `retrieves next elements`(
        list: List<Int>,
        expectedNextElements: List<Int>
    ) {
        val circularListIterator = list.circularListIterator()

        val nextElements = listOf(
            circularListIterator.next(),
            circularListIterator.next(),
            circularListIterator.next(),
            circularListIterator.next()
        )

        assertEquals(expectedNextElements, nextElements)
    }

    @ParameterizedTest
    @MethodSource("listsWithExpectedPreviousElements")
    fun `retrieves previous elements`(
        list: List<Int>,
        expectedPreviousElements: List<Int>
    ) {
        val circularListIterator = list.circularListIterator()

        val previousElements = listOf(
            circularListIterator.previous(),
            circularListIterator.previous(),
            circularListIterator.previous(),
            circularListIterator.previous()
        )

        assertEquals(expectedPreviousElements, previousElements)
    }

    @ParameterizedTest
    @MethodSource("listsWithExpectedNextAndPreviousElements")
    fun `retrieves next and previous elements`(
        list: List<Int>,
        expectedNextAndPreviousElements: List<Int>
    ) {
        val circularListIterator = list.circularListIterator()

        val nextAndPreviousElements = listOf(
            circularListIterator.next(),
            circularListIterator.next(),
            circularListIterator.previous(),
            circularListIterator.previous()
        )

        assertEquals(expectedNextAndPreviousElements, nextAndPreviousElements)
    }

    companion object {
        @JvmStatic
        fun listsWithExpectedNextElements() = listOf(
            Arguments.of(listOf(1), listOf(1, 1, 1, 1)),
            Arguments.of(listOf(1, 2), listOf(1, 2, 1, 2)),
            Arguments.of(listOf(1, 2, 3), listOf(1, 2, 3, 1))
        )

        @JvmStatic
        fun listsWithExpectedPreviousElements() = listOf(
            Arguments.of(listOf(1), listOf(1, 1, 1, 1)),
            Arguments.of(listOf(1, 2), listOf(2, 1, 2, 1)),
            Arguments.of(listOf(1, 2, 3), listOf(3, 2, 1, 3))
        )

        @JvmStatic
        fun listsWithExpectedNextAndPreviousElements() = listOf(
            Arguments.of(listOf(1), listOf(1, 1, 1, 1)),
            Arguments.of(listOf(1, 2), listOf(1, 2, 2, 1)),
            Arguments.of(listOf(1, 2, 3), listOf(1, 2, 2, 1))
        )
    }
}
