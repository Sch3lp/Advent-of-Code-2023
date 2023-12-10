package be.swsb.aoc2023.day10

import be.swsb.aoc2023.Debugging.debug
import be.swsb.aoc2023.Point

fun solve1(input:String) = PipeMap.fromString(input).countSegments() / 2
data class PipeMap(private val points: Map<Point, TilePoint>) : Map<Point, TilePoint> by points {
    private val startingPoint = points.values.first { tilePoint -> tilePoint.tile == Start }

    fun countSegments(): Int {
        val segments = mutableSetOf(startingPoint)
        var (previousSegment, currentSegment) = startingPoint to findStartPipeOptions(startingPoint).first
        while (currentSegment != startingPoint) {
            debug { "${currentSegment.tile.icon}" }
            segments.add(currentSegment)
            val (prev, cur) = currentSegment.follow(previousSegment)
                .let { (prev, nextPoint) -> prev to points.getValue(nextPoint) }
            previousSegment = prev
            currentSegment = cur
        }
        return segments.size
    }

    private fun findStartPipeOptions(startPoint: TilePoint): Pair<TilePoint, TilePoint> {
        val (top, left, right, bottom) = startPoint.point.orthogonalNeighbours.map { points.get(it) }
        val options = mutableListOf(
            top?.keepIfInOrNull(VePipe, SEBend, SWBend),
            bottom?.keepIfInOrNull(VePipe, NEBend, NWBend),
            left?.keepIfInOrNull(HoPipe, SEBend, NEBend),
            right?.keepIfInOrNull(HoPipe, NWBend, SWBend),
        ).filterNotNull()
        require(options.size == 2) { "Start does not have exactly 2 connecting tiles, impossible to have a loop." }
        return options.let { (first, second) -> first to second }
            .debug { "Found start connectors: ${it.first} and ${it.second}" }
    }

    private fun TilePoint.keepIfInOrNull(vararg tiles: Tile) = if (tile in tiles) this else null

    companion object {
        fun fromString(input: String) = PipeMap(
            input.lines().flatMapIndexed { y, line ->
                line.mapIndexed { x, c ->
                    TilePoint(Point(x, y), Tile.of(c))
                }
            }.associateBy(TilePoint::point)
        )
    }
}

data class TilePoint(val point: Point, val tile: Tile) {
    fun follow(previousSegment: TilePoint): Pair<TilePoint, Point> =
        this to tile.follow(point, previousSegment.point)
}

sealed class Tile(val icon: Char) {

    fun follow(from: Point, previous: Point): Point = connectors(from).other(previous)
    abstract fun connectors(from: Point): Pair<Point, Point>

    private fun Pair<Point, Point>.other(point: Point): Point = when (point) {
        this.first -> this.second
        this.second -> this.first
        else -> error("$this does not contain $point, so .other cannot be returned")
    }

    companion object {
        fun of(char: Char) = when (char) {
            '.' -> Ground
            '|' -> VePipe
            '-' -> HoPipe
            'L' -> NEBend
            'J' -> NWBend
            '7' -> SWBend
            'F' -> SEBend
            'S' -> Start
            else -> error("Unknown symbol: $char")
        }
    }
}

data object Ground : Tile('.') {
    override fun connectors(from: Point) = from to from
}

data object VePipe : Tile('|') {
    override fun connectors(from: Point) = from.plus(Point.at(0, 1)) to from.plus(Point.at(0, -1))
}

data object HoPipe : Tile('-') {
    override fun connectors(from: Point) = from.plus(Point.at(1, 0)) to from.plus(Point.at(-1, 0))
}

data object NEBend : Tile('L') {
    override fun connectors(from: Point) = from.plus(Point.at(0, -1)) to from.plus(Point.at(1, 0))
}

data object NWBend : Tile('J') {
    override fun connectors(from: Point) = from.plus(Point.at(-1, 0)) to from.plus(Point.at(0, -1))
}

data object SWBend : Tile('7') {
    override fun connectors(from: Point) = from.plus(Point.at(-1, 0)) to from.plus(Point.at(0, 1))
}

data object SEBend : Tile('F') {
    override fun connectors(from: Point) = from.plus(Point.at(1, 0)) to from.plus(Point.at(0, 1))
}

data object Start : Tile('S') {
    override fun connectors(from: Point) = from.plus(Point.at(0, 1)) to from.plus(Point.at(0, -1))
}