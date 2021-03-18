package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.GeometrySerializer
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmOverloads

@Serializable(with = GeometrySerializer::class)
data class MultiPolygon @JvmOverloads constructor(
    val coordinates: List<List<List<Position>>>,
    override val bbox: BoundingBox? = null
) : Geometry() {
    @JvmOverloads
    constructor(vararg coordinates: List<List<Position>>, bbox: BoundingBox? = null) : this(coordinates.toList(), bbox)

    @JvmOverloads
    constructor(
        coordinates: Array<Array<Array<DoubleArray>>>,
        bbox: BoundingBox? = null
    ) : this(coordinates.map { ring -> ring.map { it.map(::Position) } }, bbox)
}
