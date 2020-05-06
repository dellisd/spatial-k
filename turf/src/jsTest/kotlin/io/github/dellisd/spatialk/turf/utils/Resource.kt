package io.github.dellisd.spatialk.turf.utils

private external fun require(module: String): dynamic
private val fs = require("fs")

actual class Resource actual constructor(actual val name: String) {
    private val path = "$RESOURCE_PATH/$name"

    actual fun exists(): Boolean = fs.existsSync(path) as Boolean

    actual fun readText(): String = fs.readFileSync(path, "utf8") as String
}
