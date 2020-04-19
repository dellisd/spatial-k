package io.github.dellisd.geojson

import io.github.dellisd.geojson.serialization.FeatureCollectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.UnstableDefault
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
class FeatureCollection @JvmOverloads constructor(
    val features: MutableList<Feature> = mutableListOf(),
    override val bbox: BoundingBox? = null
) :
    MutableCollection<Feature> by features, GeoJson {

    @JvmOverloads
    constructor(vararg features: Feature, bbox: BoundingBox? = null) : this(features.toMutableList(), bbox)

    @UnstableDefault
    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("toJson")
    override val json: String
        get() = Json.stringify(serializer(), this)

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

    @UnstableDefault
    override fun toString(): String = json

    companion object {
        @JvmStatic
        fun serializer(): KSerializer<FeatureCollection> = FeatureCollectionSerializer

        @JvmStatic
        @UnstableDefault
        @JvmName("fromJson")
        fun String.toFeatureCollection() = Json.parse(serializer(), this)
    }
}
