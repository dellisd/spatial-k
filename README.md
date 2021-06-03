# Spatial K

Spatial K is a set of libraries for working with geospatial data in Kotlin inlcuding an implementation of GeoJson and 
a port of Turfjs written in pure Kotlin. It supports Kotlin Multiplatform and Java projects while also featuring a 
Kotlin DSL for building GeoJson objects.

See the [project site](https://dellisd.github.io/spatial-k) form more info.

## Installation

#### Java and Kotlin/JVM

```kotlin
dependencies {
    implementation("io.github.dellisd.spatialk:geojson:0.1.1")
    implementation("io.github.dellisd.spatialk:turf:0.1.1")
}
```

#### Kotlin Multiplatform
```kotlin
commonMain {
    dependencies {
        implementation("io.github.dellisd.spatialk:geojson:0.1.1")
        implementation("io.github.dellisd.spatialk:turf:0.1.1")
    }
}
```