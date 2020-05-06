package io.github.dellisd.spatialk.turf.utils

const val RESOURCE_PATH = "./src/commonTest/resources"

expect class Resource(name: String) {
    val name: String

    fun exists(): Boolean
    fun readText(): String
}

fun readResource(name: String) = Resource(name).readText()
