package com.akikanellis.adventofcode.utils

fun resourceText(resource: String): String = object {}.javaClass.getResource(resource)!!.readText()
