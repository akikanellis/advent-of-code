package com.akikanellis.adventofcode.year2022.utils

class CircularListIterator<T>(private val list: List<T>) : ListIterator<T> {
    private var index = 0

    override fun hasNext() = list.isNotEmpty()

    override fun nextIndex() = index

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException()
        val element = list[nextIndex()]
        index = (index + 1) % list.size
        return element
    }

    override fun hasPrevious() = hasNext()

    override fun previousIndex() =
        if (!hasPrevious()) {
            -1
        } else {
            (((index - 1) % list.size) + list.size) % list.size
        }

    override fun previous(): T {
        if (!hasPrevious()) throw NoSuchElementException()
        index = previousIndex()
        return list[index]
    }
}

fun <T> List<T>.circularListIterator() = CircularListIterator(this)
