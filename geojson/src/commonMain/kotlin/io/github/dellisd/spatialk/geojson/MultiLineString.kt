package io.github.dellisd.spatialk.geojson

import kotlin.jvm.JvmOverloads

data class MultiLineString @JvmOverloads constructor(
    val coordinates: List<List<Position>>,
    override val bbox: BoundingBox? = null
) : Geometry() {
    @JvmOverloads
    constructor(vararg coordinates: List<Position>, bbox: BoundingBox? = null) : this(coordinates.toList(), bbox)

    @JvmOverloads
    constructor(
        coordinates: Array<Array<DoubleArray>>,
        bbox: BoundingBox? = null
    ) : this(coordinates.map { it.map(::Position) }, bbox)

    init {
        coordinates.forEach { line ->
            if (line.size < 2) {
                throw IllegalArgumentException("LineString must have at least two positions")
            }
        }
    }
}
