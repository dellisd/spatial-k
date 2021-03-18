package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.BoundingBoxSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmSynthetic

/**
 * Represents an area bounded by a [northeast] and [southwest] [Position].
 *
 * A GeoJSON object MAY have a member named "bbox" to include information on the coordinate range for its Geometries,
 * Features, or FeatureCollections.
 *
 * When serialized, a BoundingBox is represented as an array of length 2*n where n is the number of dimensions
 * represented in the contained geometries, with all axes of the most southwesterly point followed by all axes
 * of the northeasterly point. The axes order of a BoundingBox follow the axes order of geometries.
 *
 * For the BoundingBox to be serialized in 3D form, both Positions must have a defined altitude.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-5">https://tools.ietf.org/html/rfc7946#section-5</a>
 *
 * @property northeast The northeastern corner of the BoundingBox
 * @property southwest The southwestern corner of the BoundingBox
 * @property coordinates The GeoJSON bounding box coordinate array
 */
@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
@Serializable(with = BoundingBoxSerializer::class)
@Suppress("MagicNumber")
class BoundingBox constructor(val coordinates: DoubleArray) {
    init {
        require(coordinates.size == 4 || coordinates.size == 6) {
            "Bounding Box coordinates must either have 4 or 6 values"
        }
    }

    constructor(west: Double, south: Double, east: Double, north: Double) : this(
        doubleArrayOf(west, south, east, north)
    )

    constructor(coordinates: List<Double>) : this(coordinates.toDoubleArray())

    constructor(
        west: Double,
        south: Double,
        minAltitude: Double,
        east: Double,
        north: Double,
        maxAltitude: Double
    ) : this(doubleArrayOf(west, south, minAltitude, east, north, maxAltitude))

    constructor(southwest: Position, northeast: Position) : this(
        when (southwest.hasAltitude && northeast.hasAltitude) {
            true -> southwest.coordinates + northeast.coordinates
            false -> doubleArrayOf(southwest.longitude, southwest.latitude, northeast.longitude, northeast.latitude)
        }
    )

    val southwest: Position
        get() = when (hasAltitude) {
            true -> Position(coordinates[0], coordinates[1], coordinates[2])
            false -> Position(coordinates[0], coordinates[1])
        }

    val northeast: Position
        get() = when (hasAltitude) {
            true -> Position(coordinates[3], coordinates[4], coordinates[5])
            false -> Position(coordinates[2], coordinates[3])
        }

    @JvmSynthetic
    operator fun component1(): Position = southwest

    @JvmSynthetic
    operator fun component2(): Position = northeast
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BoundingBox

        if (!coordinates.contentEquals(other.coordinates)) return false

        return true
    }

    override fun hashCode(): Int {
        return coordinates.contentHashCode()
    }

    override fun toString(): String {
        return "BoundingBox(southwest=$southwest, northeast=$northeast)"
    }
}

@Suppress("MagicNumber")
val BoundingBox.hasAltitude: Boolean
    get() = coordinates.size == 6
