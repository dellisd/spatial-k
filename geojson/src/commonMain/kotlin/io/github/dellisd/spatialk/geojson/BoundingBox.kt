package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.BoundingBoxSerializer
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmOverloads

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
 */
@Serializable(with = BoundingBoxSerializer::class)
data class BoundingBox @JvmOverloads constructor(val southwest: Position, val northeast: Position) {
    constructor(west: Double, south: Double, east: Double, north: Double) : this(
        LngLat(west, south),
        LngLat(east, north)
    )

    // Not the prettiest... but it works™
    constructor(coordinates: DoubleArray) : this(
        LngLat(
            coordinates[0],
            coordinates[1],
            when (coordinates.size) {
                4 -> null
                6 -> coordinates[2]
                else -> throw IllegalArgumentException(
                    "Invalid number of values in coordinates array"
                )
            }
        ),
        LngLat(
            when (coordinates.size) {
                4 -> coordinates[2]
                6 -> coordinates[3]
                else -> throw IllegalArgumentException(
                    "Invalid number of values in coordinates array"
                )
            },
            when (coordinates.size) {
                4 -> coordinates[3]
                6 -> coordinates[4]
                else -> throw IllegalArgumentException(
                    "Invalid number of values in coordinates array"
                )
            },
            when (coordinates.size) {
                4 -> null
                6 -> coordinates[5]
                else -> throw IllegalArgumentException(
                    "Invalid number of values in coordinates array"
                )
            }
        )
    )

    constructor(coordinates: List<Double>) : this(coordinates.toDoubleArray())

    constructor(
        west: Double,
        south: Double,
        minAltitude: Double,
        east: Double,
        north: Double,
        maxAltitude: Double
    ) : this(
        LngLat(west, south, minAltitude),
        LngLat(east, north, maxAltitude)
    )
}
