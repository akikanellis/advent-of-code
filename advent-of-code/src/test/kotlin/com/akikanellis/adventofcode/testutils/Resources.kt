package com.akikanellis.adventofcode.testutils

fun resourceText(resource: String): String = object {}.javaClass.getResource(resource)!!.readText()
