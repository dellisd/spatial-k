package io.github.dellisd.spatialk.geojson

import io.github.dellisd.spatialk.geojson.FeatureCollection.Companion.toFeatureCollection
import io.github.dellisd.spatialk.geojson.dsl.feature
import io.github.dellisd.spatialk.geojson.dsl.featureCollection
import io.github.dellisd.spatialk.geojson.dsl.lineString
import io.github.dellisd.spatialk.geojson.dsl.point
import io.github.dellisd.spatialk.geojson.dsl.polygon
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.BenchmarkTimeUnit
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlin.random.Random

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(BenchmarkTimeUnit.MILLISECONDS)
open class GeoJsonBenchmark {
    private lateinit var dataset: FeatureCollection
    private lateinit var geojson: String

    private fun generateDataset(): FeatureCollection {
        val random = Random(0)
        return featureCollection {
            repeat(5000) {
                +feature {
                    geometry = point(random.nextDouble(360.0) - 180, random.nextDouble(360.0) - 180)
                }
            }

            repeat(5000) {
                +feature {
                    geometry = lineString {
                        repeat(10) {
                            +Position(random.nextDouble(360.0) - 180, random.nextDouble(360.0) - 180)
                        }
                    }
                }
            }

            repeat(5000) {
                +feature {
                    geometry = polygon {
                        ring {
                            repeat(10) {
                                +Position(random.nextDouble(360.0) - 180, random.nextDouble(360.0) - 180)
                            }
                        }
                    }
                }
            }
        }
    }

    @Setup
    fun setup() {
        dataset = generateDataset()
        geojson = dataset.json
    }

    @Benchmark
    fun serialization() {
        val result = dataset.json
    }

    @Benchmark
    fun deserialization() {
        val result = geojson.toFeatureCollection()
    }
}
