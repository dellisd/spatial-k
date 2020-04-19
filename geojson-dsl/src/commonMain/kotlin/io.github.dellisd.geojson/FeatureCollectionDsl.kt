@file:JvmName("-FeatureCollectionDslKt")

package io.github.dellisd.geojson

import kotlin.jvm.JvmName

@GeoJsonDsl
class FeatureCollectionDsl(
    private val features: MutableList<Feature> = mutableListOf(),
    var bbox: BoundingBox? = null
) {
    operator fun Feature.unaryPlus() {
        features.add(this)
    }

    fun create(): FeatureCollection = FeatureCollection(features, bbox)
}

inline fun featureCollection(block: FeatureCollectionDsl.() -> Unit) = FeatureCollectionDsl().apply(block).create()
