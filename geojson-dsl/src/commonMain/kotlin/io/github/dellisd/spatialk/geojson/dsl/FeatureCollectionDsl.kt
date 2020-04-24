@file:JvmName("-FeatureCollectionDslKt")

package io.github.dellisd.spatialk.geojson.dsl

import io.github.dellisd.spatialk.geojson.BoundingBox
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import kotlin.jvm.JvmName

@GeoJsonDsl
class FeatureCollectionDsl(
    private val features: MutableList<Feature> = mutableListOf(),
    var bbox: BoundingBox? = null
) {
    operator fun Feature.unaryPlus() {
        features.add(this)
    }

    fun create(): FeatureCollection =
        FeatureCollection(features, bbox)
}

inline fun featureCollection(block: FeatureCollectionDsl.() -> Unit) = FeatureCollectionDsl()
    .apply(block).create()
