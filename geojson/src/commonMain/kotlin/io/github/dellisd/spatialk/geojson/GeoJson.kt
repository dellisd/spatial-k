package io.github.dellisd.spatialk.geojson

import kotlin.jvm.JvmName

/**
 * A GeoJSON object represents a [Geometry], [Feature], or [collection of Features][FeatureCollection].
 *
 * @property bbox An optional bounding box used to represent the limits of the object's geometry.
 * @property json Gets a JSON representation of this object.
 */
interface GeoJson {
    val bbox: BoundingBox?

    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("toJson")
    val json: String
}
