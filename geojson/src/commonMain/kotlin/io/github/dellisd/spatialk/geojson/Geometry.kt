package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.GeometrySerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

private fun DoubleArray.toLngLat() = LngLat(this[0], this[1], this.getOrNull(2))

@Serializable(with = GeometrySerializer::class)
sealed class Geometry : GeoJson {
    abstract override val bbox: BoundingBox?

    @UnstableDefault
    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("toJson")
    override val json: String
        get() = Json.stringify(serializer(), this)

    @UnstableDefault
    override fun toString(): String = json
}

class Point @JvmOverloads constructor(val coordinates: Position, override val bbox: BoundingBox? = null) :
    Geometry(),
    Position by coordinates {

    @JvmOverloads
    constructor(coordinates: DoubleArray, bbox: BoundingBox? = null) : this(coordinates.toLngLat(), bbox)

    operator fun component4() = bbox

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Point

        if (coordinates != other.coordinates) return false

        return true
    }

    override fun hashCode(): Int = coordinates.hashCode()
}

data class MultiPoint @JvmOverloads constructor(
    val coordinates: List<Position>,
    override val bbox: BoundingBox? = null
) :
    Geometry() {
    @JvmOverloads
    constructor(vararg coordinates: Position, bbox: BoundingBox? = null) : this(coordinates.toList(), bbox)

    @JvmOverloads
    constructor(
        coordinates: Array<DoubleArray>,
        bbox: BoundingBox? = null
    ) : this(coordinates.map(DoubleArray::toLngLat), bbox)
}

data class LineString @JvmOverloads constructor(
    val coordinates: List<Position>,
    override val bbox: BoundingBox? = null
) :
    Geometry() {
    @JvmOverloads
    constructor(vararg coordinates: Position, bbox: BoundingBox? = null) : this(coordinates.toList(), bbox)

    @JvmOverloads
    constructor(
        coordinates: Array<DoubleArray>,
        bbox: BoundingBox? = null
    ) : this(coordinates.map(DoubleArray::toLngLat), bbox)

    init {
        if (coordinates.size < 2) {
            throw IllegalArgumentException("LineString must have at least two positions")
        }
    }
}

data class MultiLineString @JvmOverloads constructor(
    val coordinates: List<List<Position>>,
    override val bbox: BoundingBox? = null
) :
    Geometry() {
    @JvmOverloads
    constructor(vararg coordinates: List<Position>, bbox: BoundingBox? = null) : this(coordinates.toList(), bbox)

    @JvmOverloads
    constructor(
        coordinates: Array<Array<DoubleArray>>,
        bbox: BoundingBox? = null
    ) : this(coordinates.map { it.map(DoubleArray::toLngLat) }, bbox)

    init {
        coordinates.forEach { line ->
            if (line.size < 2) {
                throw IllegalArgumentException("LineString must have at least two positions")
            }
        }
    }
}

data class Polygon @JvmOverloads constructor(
    val coordinates: List<List<Position>>,
    override val bbox: BoundingBox? = null
) :
    Geometry() {
    @JvmOverloads
    constructor(vararg coordinates: List<Position>, bbox: BoundingBox? = null) : this(coordinates.toList(), bbox)

    @JvmOverloads
    constructor(
        coordinates: Array<Array<DoubleArray>>,
        bbox: BoundingBox? = null
    ) : this(coordinates.map { it.map(DoubleArray::toLngLat) }, bbox)
}

data class MultiPolygon @JvmOverloads constructor(
    val coordinates: List<List<List<Position>>>,
    override val bbox: BoundingBox? = null
) :
    Geometry() {
    @JvmOverloads
    constructor(vararg coordinates: List<List<Position>>, bbox: BoundingBox? = null) : this(coordinates.toList(), bbox)

    @JvmOverloads
    constructor(
        coordinates: Array<Array<Array<DoubleArray>>>,
        bbox: BoundingBox? = null
    ) : this(coordinates.map { ring -> ring.map { it.map(DoubleArray::toLngLat) } }, bbox)
}

data class GeometryCollection @JvmOverloads constructor(
    val geometries: List<Geometry>,
    override val bbox: BoundingBox? = null
) :
    Geometry(), Collection<Geometry> by geometries {
    @JvmOverloads
    constructor(vararg geometries: Geometry, bbox: BoundingBox? = null) : this(geometries.toList(), bbox)
}
