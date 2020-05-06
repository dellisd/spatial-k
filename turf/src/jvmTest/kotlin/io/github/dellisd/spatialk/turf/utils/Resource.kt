package io.github.dellisd.spatialk.turf.utils

import java.io.File

actual class Resource actual constructor(actual val name: String) {
    private val file = File("$RESOURCE_PATH/$name")

    actual fun exists(): Boolean = file.exists()

    actual fun readText(): String = file.readText()
}
