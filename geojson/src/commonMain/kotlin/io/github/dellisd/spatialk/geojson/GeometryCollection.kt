package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.GeometrySerializer
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmOverloads

@Serializable(with = GeometrySerializer::class)
class GeometryCollection @JvmOverloads constructor(
    val geometries: List<Geometry>,
    override val bbox: BoundingBox? = null
) : Geometry(), Collection<Geometry> by geometries {
    @JvmOverloads
    constructor(vararg geometries: Geometry, bbox: BoundingBox? = null) : this(geometries.toList(), bbox)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GeometryCollection

        if (geometries != other.geometries) return false
        if (bbox != other.bbox) return false

        return true
    }

    override fun hashCode(): Int {
        var result = geometries.hashCode()
        result = 31 * result + (bbox?.hashCode() ?: 0)
        return result
    }
}
