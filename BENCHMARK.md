# Benchmarks

Benchmarks are set up for GeoJSON serialization and deserialization.

## Running Benchmarks

```shell
./gradlew :geojson:benchmark
```

This will run benchmarks on the JVM, NodeJS, and Kotlin/Native.

These benchmarks measure the time taken to serialize and deserialize a `FeatureCollection` containing 15,000 randomly
generated features.
See [GeoJsonBenchmark.kt](geojson/src/commonBench/kotlin/io/github/dellisd/spatialk/geojson/GeoJsonBenchmark.kt) for
details.

## Results

All measurements are in ms/op (milliseconds per operation). Lower score is better.

| Target              | Serialization         | Deserialization     |
|---------------------|-----------------------|---------------------|
| JVM                 | `834.176 ± 55.719`    | `77.700 ± 1.123`    |
| JS                  | `3,287.40 ± 8.763`    | `423.472 ± 17.805`  |
| Native (`linuxX64`) | `5624.209 ± 1242.046` | `993.008 ± 243.719` |

_Run on Ubuntu 20.04 (WSL2). 32GB RAM, 3.6 GHz 8-core Intel Core i7_
