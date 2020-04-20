#TODO: Library Name Here

This library is an implementation of GeoJSON and port of Turfjs written in pure Kotlin. It supports Kotlin Multiplatform
and Java projects and also features a Kotlin DSL for building GeoJSON objects.

## Installation

#### Java and Kotlin/JVM

```groovy
dependencies {
    implementation "package.name.here:geojson:0.1.0"
    implementation "package.name.here:turf:0.1.0"

    // Kotlin only
    implementation "package.name.here:geojson-dsl:0.1.0" 
}
```

#### Kotlin Multiplatform
```groovy
commonMain {
    dependencies {
        implementation "package.name.here:geojson:0.1.0"
        implementation "package.name.here:turf:0.1.0"
        implementation "package.name.here:geojson-dsl:0.1.0" 
    }
}
```