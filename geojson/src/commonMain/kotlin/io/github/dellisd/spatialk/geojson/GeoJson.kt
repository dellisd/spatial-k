package io.github.dellisd.spatialk.geojson

import kotlinx.serialization.json.JsonElement

/**
 * A GeoJSON object represents a [Geometry], [Feature], or [collection of Features][FeatureCollection].
 *
 * @property bbox An optional bounding box used to represent the limits of the object's geometry.
 */
interface GeoJson {
    val bbox: BoundingBox?
    val foreignMembers: MutableMap<String, JsonElement>

    /**
     * Gets a JSON representation of this object.
     * @return JSON representation
     */
    fun json(): String
}
