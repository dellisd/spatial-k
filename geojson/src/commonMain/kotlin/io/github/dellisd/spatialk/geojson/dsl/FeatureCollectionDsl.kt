@file:JvmName("-FeatureCollectionDslKt")

package io.github.dellisd.spatialk.geojson.dsl

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.Geometry
import kotlinx.serialization.json.JsonElement
import kotlin.jvm.JvmName

@GeoJsonDsl
class FeatureCollectionDsl(
    private val features: MutableList<Feature> = mutableListOf(),
    var bbox: BoundingBox? = null,
    val foreignMembers: MutableMap<String, JsonElement> = mutableMapOf()
) {
    operator fun Feature.unaryPlus() {
        features.add(this)
    }

    fun create(): FeatureCollection =
        FeatureCollection(features, bbox, foreignMembers)

    fun feature(
        geometry: Geometry? = null,
        id: String? = null,
        bbox: BoundingBox? = null,
        foreignMembers: ForeignMembersBuilder.() -> Unit = {},
        properties: PropertiesBuilder.() -> Unit = {}
    ) {
        +io.github.dellisd.spatialk.geojson.dsl.feature(geometry = geometry,
                                                        id = id,
                                                        bbox = bbox,
                                                        foreignMembers = foreignMembers,
                                                        properties = properties)
    }

    fun foreignMembers(foreignMembers: ForeignMembersBuilder.() -> Unit = {}) {
        this.foreignMembers.putAll(ForeignMembersBuilder().apply(foreignMembers).build())
    }
}

@GeoJsonDsl
inline fun featureCollection(block: FeatureCollectionDsl.() -> Unit) = FeatureCollectionDsl()
    .apply(block).create()
