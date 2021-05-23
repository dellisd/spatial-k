package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.GeometrySerializer
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmOverloads

@Serializable(with = GeometrySerializer::class)
data class GeometryCollection @JvmOverloads constructor(
    val geometries: List<Geometry>,
    override val bbox: BoundingBox? = null
) : Geometry(), Collection<Geometry> by geometries {
    @JvmOverloads
    constructor(vararg geometries: Geometry, bbox: BoundingBox? = null) : this(geometries.toList(), bbox)
}
