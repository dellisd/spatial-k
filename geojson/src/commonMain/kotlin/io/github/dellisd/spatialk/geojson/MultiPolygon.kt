package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.GeometrySerializer
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmOverloads

@Serializable(with = GeometrySerializer::class)
class MultiPolygon @JvmOverloads constructor(
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MultiPolygon

        if (coordinates != other.coordinates) return false
        if (bbox != other.bbox) return false

        return true
    }

    override fun hashCode(): Int {
        var result = coordinates.hashCode()
        result = 31 * result + (bbox?.hashCode() ?: 0)
        return result
    }
}
