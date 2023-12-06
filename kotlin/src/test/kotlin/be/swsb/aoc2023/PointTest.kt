package be.swsb.aoc2023

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PointTest {

    @Test
    fun `given two points - we can add them together`() {
        val origin = Point(0, 0)
        val vector = Point(-1, 1)

        val actual = origin + vector

        actual shouldBe Point(-1, 1)
    }

    @Test
    fun `return neighbouring points of point`() {
        val point = Point(0, 0)

        val actual = point.neighbours

        actual.shouldContainExactlyInAnyOrder(
            Point(-1, -1), Point(0, -1), Point(1, -1),
            Point(-1, 0), Point(1, 0),
            Point(-1, 1), Point(0, 1), Point(1, 1)
        )
    }

    @Test
    fun `return orthogonal neighbouring points of point`() {
        val point = Point(0, 0)

        val actual = point.orthogonalNeighbours

        actual.shouldContainExactlyInAnyOrder(
            Point(0, -1),
            Point(-1, 0), Point(1, 0),
            Point(0, 1)
        )
    }

    @Test
    fun `rangeTo retains expected order`() {
        (Point(0, 0)..Point(0, 3)).shouldContainExactly(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3))
        (Point(0, 3)..Point(0, 0)).shouldContainExactly(Point(0, 3), Point(0, 2), Point(0, 1), Point(0, 0))
        (Point(0, 0)..Point(3, 0)).shouldContainExactly(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0))
        (Point(3, 0)..Point(0, 0)).shouldContainExactly(Point(3, 0), Point(2, 0), Point(1, 0), Point(0, 0))
    }

    @Test
    fun `rangeTo diagonally just goes diagonally (no manhattan)`() {
        (Point(0, 0)..Point(1, 2)).shouldContainExactly(Point(0, 0), Point(1, 1), Point(1, 2))
        (Point(0, 0)..Point(2, 1)).shouldContainExactly(Point(0, 0), Point(1, 1), Point(2, 1))

        (Point(2, 1)..Point(0, 0)).shouldContainExactly(Point(2, 1), Point(1, 0), Point(0, 0))
        (Point(1, 2)..Point(0, 0)).shouldContainExactly(Point(1, 2), Point(0, 1), Point(0, 0))

        (Point(0, 0)..Point(-1, -2)).shouldContainExactly(Point(0, 0), Point(-1, -1), Point(-1, -2))
        (Point(0, 0)..Point(-2, -1)).shouldContainExactly(Point(0, 0), Point(-1, -1), Point(-2, -1))

        (Point(-1, -2)..Point(0, 0)).shouldContainExactly(Point(-1, -2), Point(0, -1), Point(0, 0))
        (Point(-2, -1)..Point(0, 0)).shouldContainExactly(Point(-2, -1), Point(-1, 0), Point(0, 0))

        (Point(0, 0)..Point(1, -2)).shouldContainExactly(Point(0, 0), Point(1, -1), Point(1, -2))
        (Point(0, 0)..Point(-2, 1)).shouldContainExactly(Point(0, 0), Point(-1, 1), Point(-2, 1))

        (Point(1, -2)..Point(0, 0)).shouldContainExactly(Point(1, -2), Point(0, -1), Point(0, 0))
        (Point(-2, 1)..Point(0, 0)).shouldContainExactly(Point(-2, 1), Point(-1,  0), Point(0, 0))
    }
}