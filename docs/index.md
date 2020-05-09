<p align="center">
    <img width="200" src="images/colour.svg" alt="Spatial K Logo" title="Part of a complete breakfast">
</p>

# Spatial K

Spatial K is a set of libraries for working with geospatial data in Kotlin inlcuding an implementation of GeoJson and 
a port of Turfjs written in pure Kotlin. It supports Kotlin Multiplatform and Java projects while also featuring a 
Kotlin DSL for building GeoJson objects.

## Installation

#### Java and Kotlin/JVM

```groovy
dependencies {
    implementation "io.github.dellisd.spatialk:geojson:0.1.0"
    implementation "io.github.dellisd.spatialk:turf:0.1.0"

    // Kotlin only
    implementation "io.github.dellisd.spatialk:geojson-dsl:0.1.0" 
}
```

#### Kotlin Multiplatform
```groovy
commonMain {
    dependencies {
        implementation "io.github.dellisd.spatialk:geojson:0.1.0"
        implementation "io.github.dellisd.spatialk:turf:0.1.0"
        implementation "io.github.dellisd.spatialk:geojson-dsl:0.1.0" 
    }
}
```

### Snapshots

Snapshot builds are available on Sonatype.

```groovy
repositories {
    maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
}
```

## Supported targets

Spatial K currently supports the following platform targets: `jvm`, `js`, `mingwX64`, `linuxX64`, `macosX64`, `iosX64`, `iosArm64`, and `iosArm32`.