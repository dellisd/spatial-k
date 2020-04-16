package io.github.dellisd.geojson

interface Position {
    val latitude: Double
    val longitude: Double
    val altitude: Double

    operator fun component1(): Double
    operator fun component2(): Double
    operator fun component3(): Double
}
