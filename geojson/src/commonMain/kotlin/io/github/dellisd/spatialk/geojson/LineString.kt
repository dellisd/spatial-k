package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.GeometrySerializer
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmOverloads

@Serializable(with = GeometrySerializer::class)
data class LineString @JvmOverloads constructor(
    val coordinates: List<Position>,
    override val bbox: BoundingBox? = null
) : Geometry() {
    @JvmOverloads
    constructor(vararg coordinates: Position, bbox: BoundingBox? = null) : this(coordinates.toList(), bbox)

    @JvmOverloads
    constructor(
        coordinates: Array<DoubleArray>,
        bbox: BoundingBox? = null
    ) : this(coordinates.map(::Position), bbox)

    init {
        if (coordinates.size < 2) {
            throw IllegalArgumentException("LineString must have at least two positions")
        }
    }
}
