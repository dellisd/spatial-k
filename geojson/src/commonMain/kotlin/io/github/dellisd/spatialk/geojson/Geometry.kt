package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.serialization.GeometrySerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

@Serializable(with = GeometrySerializer::class)
sealed class Geometry protected constructor() : GeoJson {
    abstract override val bbox: BoundingBox?

    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("toJson")
    override val json: String
        get() = Json.encodeToString(serializer(), this)

    override fun toString(): String = json
}



