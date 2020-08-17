package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.FeatureSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonPrimitive
import kotlin.collections.set
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

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
@Suppress("TooManyFunctions")
class Feature @JvmOverloads constructor(
    val geometry: Geometry?,
    properties: Map<String, JsonElement> = emptyMap(),
    val id: String? = null,
    override val bbox: BoundingBox? = null
) : GeoJson {
    private val _properties: MutableMap<String, JsonElement> = properties.toMutableMap()
    val properties: Map<String, JsonElement> get() = _properties

    fun setStringProperty(key: String, value: String?) {
        _properties[key] = JsonPrimitive(value)
    }

    fun setNumberProperty(key: String, value: Number?) {
        _properties[key] = JsonPrimitive(value)
    }

    fun setBooleanProperty(key: String, value: Boolean?) {
        _properties[key] = JsonPrimitive(value)
    }

    fun setJsonProperty(key: String, value: JsonElement) {
        _properties[key] = value
    }

    fun getStringProperty(key: String): String? = properties[key]?.jsonPrimitive?.content

    fun getNumberProperty(key: String): Number? = properties[key]?.jsonPrimitive?.double

    fun getBooleanProperty(key: String): Boolean? = properties[key]?.jsonPrimitive?.boolean

    fun getJsonProperty(key: String): JsonElement? = properties[key]

    fun removeProperty(key: String): Any? = _properties.remove(key)

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
        if (id != other.id) return false
        if (bbox != other.bbox) return false
        if (_properties != other._properties) return false

        return true
    }

    override fun hashCode(): Int {
        var result = geometry?.hashCode() ?: 0
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (bbox?.hashCode() ?: 0)
        result = 31 * result + _properties.hashCode()
        return result
    }

    operator fun component1() = geometry
    operator fun component2() = properties
    operator fun component3() = id
    operator fun component4() = bbox

    override fun toString(): String = json

    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("toJson")
    override val json: String
        get() = Json.encodeToString(serializer(), this)

    companion object {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        @JvmStatic
        fun serializer(): KSerializer<Feature> = FeatureSerializer

        @JvmStatic
        @JvmName("fromJson")
        fun String.toFeature(): Feature = Json.decodeFromString(serializer(), this)
    }
}
