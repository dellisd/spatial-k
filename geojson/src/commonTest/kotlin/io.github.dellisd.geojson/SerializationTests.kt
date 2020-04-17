package io.github.dellisd.geojson

import io.github.dellisd.geojson.serialization.serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@UnstableDefault
@Suppress("MagicNumber")
class SerializationTests {

    @Test
    fun testSerializePosition() {
        val position = LngLat(-75.1, 45.1)
        val result = Json.stringify(Position.serializer(), position)
        assertEquals("[-75.1,45.1]", result)

        val altitude = LngLat(60.2, 23.2354, 100.5)
        val altitudeResult = Json.stringify(Position.serializer(), altitude)
        assertEquals("[60.2,23.2354,100.5]", altitudeResult)

        val list = listOf(LngLat(12.3, 45.6), LngLat(78.9, 12.3))
        val listResult = Json.stringify(Position.serializer().list, list)
        assertEquals("[[12.3,45.6],[78.9,12.3]]", listResult)
    }

    @Test
    fun testDeserializePosition() {
        val position = Json.parse(Position.serializer(), "[32.4,54.1234]")
        assertEquals(LngLat(32.4, 54.1234), position)

        val altitude = Json.parse(Position.serializer(), "[60.2,23.2354,100.5]")
        assertEquals(LngLat(60.2, 23.2354, 100.5), altitude)

        val list = Json.parse(Position.serializer().list, "[[12.3,45.6],[78.9,12.3]]")
        assertEquals(listOf(LngLat(12.3, 45.6), LngLat(78.9, 12.3)), list)
    }

    @Test
    fun testSerializeBoundingBox() {
        val bbox = BoundingBox(LngLat(-10.5, -10.5), LngLat(10.5, 10.5))
        val result = Json.stringify(BoundingBox.serializer(), bbox)
        assertEquals("[-10.5,-10.5,10.5,10.5]", result)

        val bbox3D = BoundingBox(LngLat(-10.5, -10.5, -100.8), LngLat(10.5, 10.5, 5.5))
        val result3D = Json.stringify(BoundingBox.serializer(), bbox3D)
        assertEquals("[-10.5,-10.5,-100.8,10.5,10.5,5.5]", result3D)

        // One altitude unspecified
        val bboxFake3D = BoundingBox(LngLat(-10.5, -10.5, -100.8), LngLat(10.5, 10.5))
        val fakeResult = Json.stringify(BoundingBox.serializer(), bboxFake3D)
        assertEquals("[-10.5,-10.5,10.5,10.5]", fakeResult)
    }

    @Test
    fun testDeserializeBoundingBox() {
        val bbox = Json.parse(BoundingBox.serializer(), "[-10.5,-10.5,10.5,10.5]")
        assertEquals(BoundingBox(LngLat(-10.5, -10.5), LngLat(10.5, 10.5)), bbox)

        val bbox3D = Json.parse(BoundingBox.serializer(), "[-10.5,-10.5,-100.8,10.5,10.5,5.5]")
        assertEquals(BoundingBox(LngLat(-10.5, -10.5, -100.8), LngLat(10.5, 10.5, 5.5)), bbox3D)

        assertFailsWith<SerializationException> {
            Json.parse(BoundingBox.serializer(), "[12.3]")
        }
    }
}
