# Ported Functions

The following functions have been ported as of version `0.0.3-SNAPSHOT` of this library.

You can view porting progress for the next release [here](https://github.com/dellisd/spatial-k/milestone/1).

## Measurement

- [x] [`along`](../api/turf/io.github.dellisd.spatialk.turf/along/)
- [x] [`area`](../api/turf/io.github.dellisd.spatialk.turf/area/)
- [x] [`bbox`](../api/turf/io.github.dellisd.spatialk.turf/bbox/)
- [x] [`bboxPolygon`](../api/turf/io.github.dellisd.spatialk.turf/bbox-polygon/)
- [x] [`bearing`](../api/turf/io.github.dellisd.spatialk.turf/bearing/)
- [ ] `center`
- [ ] `centerOfMass`
- [x] [`destination`](../api/turf/io.github.dellisd.spatialk.turf/destination/)
- [x] [`distance`](../api/turf/io.github.dellisd.spatialk.turf/distance/)
- [ ] `envelope`
- [x] [`length`](../api/turf/io.github.dellisd.spatialk.turf/length/)
- [x] [`midpoint`](../api/turf/io.github.dellisd.spatialk.turf/midpoint/)
- [ ] `pointOnFeature`
- [ ] `polygonTangents`
- [ ] `pointToLineDistance`
- [ ] `rhumbBearing`
- [ ] `rhumbDestination`
- [ ] `rhumbDistance`
- [ ] `square`
- [ ] `greatCircle`

## Coordinate Mutation

- [ ] `cleanCoords`
- [ ] `flip`
- [ ] `rewind`
- [x] `round`  
Use `round` or `Math.round` from the standard library instead.
- [ ] `truncate`

## Transformation

- [ ] `bboxClip`
- [ ] `bezierSpline`
- [ ] `buffer`
- [ ] `circle`
- [ ] `clone`
- [ ] `concave`
- [ ] `convex`
- [ ] `difference`
- [ ] `dissolve`
- [ ] `intersect`
- [ ] `lineOffset`
- [ ] `simplify`
- [ ] `tessellate`
- [ ] `transformRotate`
- [ ] `transformTranslate`
- [ ] `transformScale`
- [ ] `union`
- [ ] `voronoi`

## Feature Conversion

- [ ] `combine`
- [ ] `explode`
- [ ] `flatten`
- [ ] `lineToPolygon`
- [ ] `polygonize`
- [ ] `polygonToLine`

## Miscellaneous

- [ ] `kinks`
- [ ] `lineArc`
- [ ] `lineChunk`
- [x] [`lineIntersect`](../api/turf/io.github.dellisd.spatialk.turf/line-intersect/)
  Partially implemented.
- [ ] `lineOverlap`
- [ ] `lineSegment`
- [x] [`lineSlice`](../api/turf/io.github.dellisd.spatialk.turf/line-slice/)
- [ ] `lineSliceAlong`
- [ ] `lineSplit`
- [ ] `mask`
- [x] [`nearestPointOnLine`](../api/turf/io.github.dellisd.spatialk.turf/nearest-point-on-line/)
- [ ] `sector`
- [ ] `shortestPath`
- [ ] `unkinkPolygon`

## Helper

Use [`geojson-dsl`](../geojson-dsl/) instead.

## Random

- [ ] `randomPosition`
- [ ] `randomPoint`
- [ ] `randomLineString`
- [ ] `randomPolygon`

## Data

- [ ] `sample`

## Interpolation

- [ ] `interpolate`
- [ ] `isobands`
- [ ] `isolines`
- [ ] `planepoint`
- [ ] `tin`

## Joins

- [ ] `pointsWithinPolygon`
- [ ] `tag`

## Grids

- [ ] `hexGrid`
- [ ] `pointGrid`
- [ ] `squareGrid`
- [ ] `triangleGrid`

## Classification

- [ ] `nearestPoint`

## Aggregation

- [ ] `collect`
- [ ] `clustersDbscan`
- [ ] `clustersKmeans`

## Meta

- [ ] `coordAll`
- [ ] `coordEach`
- [ ] `coordReduce`
- [ ] `featureEach`
- [ ] `featureReduce`
- [ ] `flattenEach`
- [ ] `flattenReduce`
- [ ] `getCoord`
- [ ] `getCoords`
- [ ] `getGeom`
- [ ] `getType`
- [ ] `geomEach`
- [ ] `geomReduce`
- [ ] `propEach`
- [ ] `segmentEach`
- [ ] `segmentReduce`
- [ ] `getCluster`
- [ ] `clusterEach`
- [ ] `clusterReduce`

## Assertations

- [ ] `collectionOf`
- [ ] `containsNumber`
- [ ] `geojsonType`
- [ ] `featureOf`

## Booleans

- [ ] `booleanClockwise`
- [ ] `booleanContains`
- [ ] `booleanCrosses`
- [ ] `booleanDisjoint`
- [ ] `booleanEqual`
- [ ] `booleanOverlap`
- [ ] `booleanParallel`
- [ ] `booleanPointInPolygon`
- [ ] `booleanPointOnLine`
- [ ] `booleanWithin`

## Unit Conversion

- [x] [`bearingToAzimuth`](../api/turf/io.github.dellisd.spatialk.turf/bearing-to-azimuth/)
- [x] [`convertArea`](../api/turf/io.github.dellisd.spatialk.turf/convert-area/)
- [x] [`convertLength`](../api/turf/io.github.dellisd.spatialk.turf/convert-length/)
- [ ] `degreesToRadians`
- [x] [`lengthToRadians`](../api/turf/io.github.dellisd.spatialk.turf/length-to-radians/)
- [x] [`lengthToDegrees`](../api/turf/io.github.dellisd.spatialk.turf/length-to-degrees/)
- [x] [`radiansToLength`](../api/turf/io.github.dellisd.spatialk.turf/radians-to-length/)
- [ ] `radiansToDegrees`
- [ ] `toMercator`
- [ ] `toWgs84`