package be.swsb.aoc2023

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PointTest {

    @Test
    fun `given two points - we can add them together`() {
        val origin = Point(0, 0)
        val vector = Point(-1, 1)

        val actual = origin + vector

        assertThat(actual).isEqualTo(Point(-1, 1))
    }

    @Test
    fun `return neighbouring points of point`() {
        val point = Point(0, 0)

        val actual = point.neighbours

        assertThat(actual).containsExactlyInAnyOrder(
            Point(-1, -1), Point(0, -1), Point(1, -1),
            Point(-1, 0), Point(1, 0),
            Point(-1, 1), Point(0, 1), Point(1, 1)
        )
    }

    @Test
    fun `return orthogonal neighbouring points of point`() {
        val point = Point(0, 0)

        val actual = point.orthogonalNeighbours

        assertThat(actual).containsExactlyInAnyOrder(
            Point(0, -1),
            Point(-1, 0), Point(1, 0),
            Point(0, 1)
        )
    }

    @Test
    fun `rangeTo retains expected order`() {
        assertThat(Point(0, 0)..Point(0, 3)).containsExactly(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3))
        assertThat(Point(0, 3)..Point(0, 0)).containsExactly(Point(0, 3), Point(0, 2), Point(0, 1), Point(0, 0))
        assertThat(Point(0, 0)..Point(3, 0)).containsExactly(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0))
        assertThat(Point(3, 0)..Point(0, 0)).containsExactly(Point(3, 0), Point(2, 0), Point(1, 0), Point(0, 0))
    }

    @Test
    fun `rangeTo diagonally just goes diagonally (no manhattan)`() {
        assertThat(Point(0, 0)..Point(1, 2)).containsExactly(Point(0, 0), Point(1, 1), Point(1, 2))
        assertThat(Point(0, 0)..Point(2, 1)).containsExactly(Point(0, 0), Point(1, 1), Point(2, 1))

        assertThat(Point(2, 1)..Point(0, 0)).containsExactly(Point(2, 1), Point(1, 0), Point(0, 0))
        assertThat(Point(1, 2)..Point(0, 0)).containsExactly(Point(1, 2), Point(0, 1), Point(0, 0))

        assertThat(Point(0, 0)..Point(-1, -2)).containsExactly(Point(0, 0), Point(-1, -1), Point(-1, -2))
        assertThat(Point(0, 0)..Point(-2, -1)).containsExactly(Point(0, 0), Point(-1, -1), Point(-2, -1))

        assertThat(Point(-1, -2)..Point(0, 0)).containsExactly(Point(-1, -2), Point(0, -1), Point(0, 0))
        assertThat(Point(-2, -1)..Point(0, 0)).containsExactly(Point(-2, -1), Point(-1, 0), Point(0, 0))

        assertThat(Point(0, 0)..Point(1, -2)).containsExactly(Point(0, 0), Point(1, -1), Point(1, -2))
        assertThat(Point(0, 0)..Point(-2, 1)).containsExactly(Point(0, 0), Point(-1, 1), Point(-2, 1))

        assertThat(Point(1, -2)..Point(0, 0)).containsExactly(Point(1, -2), Point(0, -1), Point(0, 0))
        assertThat(Point(-2, 1)..Point(0, 0)).containsExactly(Point(-2, 1), Point(-1,  0), Point(0, 0))
    }
}