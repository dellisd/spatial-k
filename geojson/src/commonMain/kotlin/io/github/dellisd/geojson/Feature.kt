package io.github.dellisd.geojson

import kotlin.collections.MutableMap
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/**
 * A feature object represents a spatially bounded thing.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946#section-3.2">https://tools.ietf.org/html/rfc7946#section-3.2</a>
 *
 * @property geometry A [Geometry] object contained within the feature.
 * @property properties Additional properties about this feature.
 * When serialized, any non-simple types will be serialized into JSON objects.
 * @property id An optionally included string that commonly identifies this feature.
 */
class Feature @JvmOverloads constructor(
    var geometry: Geometry?,
    val properties: MutableMap<String, Any?> = mutableMapOf(),
    var id: String? = null,
    override val bbox: BoundingBox? = null
) : GeoJson {

    /**
     * Sets a property of this feature.
     * If the [key] for this property does not already exist in the feature's [properties] then it will be added.
     *
     * @param key The string key for the property
     * @param value The value of the property.
     */
    fun setProperty(key: String, value: Any?) {
        properties[key] = value
    }

    /**
     * Removes the property for the given key from this feature and returns its value.
     *
     * @param key The string key for the property
     * @return The value of the property that was removed, or null if no value was removed.
     */
    fun removeProperty(key: String): Any? = properties.remove(key)

    /**
     * Removes the property for the given key from this feature and returns its value.
     * Convenience method for remove a property and casting the result in one call.
     *
     * @param key The string key for the property
     * @param T The type of the value stored in [key]
     * @return The value of the property that was removed, or null if no value was removed.
     */
    @JvmName("removePropertyCast")
    inline fun <reified T : Any?> removeProperty(key: String) = properties.remove(key) as T?

    /**
     * Gets the value of the property with the given [key].
     *
     * @param key The string key for the property
     * @return The value of the property, or null if the key had no value.
     */
    fun getProperty(key: String): Any? = properties[key]

    /**
     * Gets the value of the property with the given [key].
     *
     * @param key The string key for the property
     * @return The value of the property cast to [T]?, or null if the key had no value.
     */
    @JvmName("getPropertyCast")
    inline fun <reified T : Any?> getProperty(key: String) = properties[key] as T?

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Feature

        if (geometry != other.geometry) return false
        if (properties != other.properties) return false
        if (id != other.id) return false
        if (bbox != other.bbox) return false

        return true
    }

    override fun hashCode(): Int {
        var result = geometry?.hashCode() ?: 0
        result = 31 * result + properties.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (bbox?.hashCode() ?: 0)
        return result
    }
}
