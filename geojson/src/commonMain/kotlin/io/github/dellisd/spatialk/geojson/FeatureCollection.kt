package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.FeatureCollectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * A FeatureCollection object is a collection of [Feature] objects.
 * This class implements the [Collection] interface and can be used as a Collection directly.
 * The list of features contained in this collection are also accessible through the [features] property.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.3">https://tools.ietf.org/html/rfc7946#section-3.3</a>
 *
 * @property features The collection of [Feature] objects stored in this collection
 */
class FeatureCollection(
    val features: List<Feature> = emptyList(),
    override val bbox: BoundingBox? = null
) : Collection<Feature> by features, GeoJson {

    constructor(vararg features: Feature, bbox: BoundingBox? = null) : this(features.toMutableList(), bbox)

    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("toJson")
    override val json: String
        get() = Json.encodeToString(serializer(), this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FeatureCollection

        if (features != other.features) return false
        if (bbox != other.bbox) return false

        return true
    }

    override fun hashCode(): Int {
        var result = features.hashCode()
        result = 31 * result + (bbox?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String = json

    operator fun component1(): List<Feature> = features
    operator fun component2(): BoundingBox? = bbox

    companion object {
        @JvmStatic
        fun serializer(): KSerializer<FeatureCollection> = FeatureCollectionSerializer

        @JvmStatic
        @JvmName("fromJson")
        fun String.toFeatureCollection() = Json.decodeFromString(serializer(), this)
    }
}
