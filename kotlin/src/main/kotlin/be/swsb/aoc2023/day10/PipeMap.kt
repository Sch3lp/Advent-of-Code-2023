package be.swsb.aoc2023.day10

import be.swsb.aoc2023.Debugging.debug
import be.swsb.aoc2023.Point
import be.swsb.aoc2023.Point.Companion.at

fun solve1(input: String) = PipeMap.fromString(input).countSegments() / 2
fun solve2(input: String) = PipeMap.fromString(input).enhance().containedPoints().size

data class PipeMap(private val points: Map<Point, TilePoint>) {
    private val startingPoint = points.values.first { tilePoint -> tilePoint.tile == Start }
    private val maxX = points.keys.maxBy { it.x }.x
    private val maxY = points.keys.maxBy { it.y }.y

    fun countSegments(): Int = pipelinePoints.size

    private val pipelinePoints by lazy {
        val segments = mutableSetOf(startingPoint)
        var (previousSegment, currentSegment) = startingPoint to findStartPipeOptions(startingPoint).first
        while (currentSegment != startingPoint) {
            segments.add(currentSegment)
            val (prev, cur) = currentSegment.follow(previousSegment)
                .let { (prev, nextPoint) -> prev to points.getValue(nextPoint) }
            previousSegment = prev
            currentSegment = cur
        }
        segments.map { it.point }.toSet()
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

    fun enhance(): PipeMap =
        copy(points = points.mapValues { (k, v) ->
            if (k in pipelinePoints) v
            else TilePoint(k, Ground)
        })

    fun containedPoints(): Set<Point> =
        (-1..maxY + 1).flatMap { y ->
            (-1..maxX + 1).map { x -> at(x, y) }.windowed(3).fold(TraceAccumulator()) { acc, points ->
                val left = points.getOrNull(0)
                val tracePoint = points.getOrNull(1)
                val right = points.getOrNull(2)
                val (intersections, containedPoints) = acc
                debug { "checking for intersection at $tracePoint" }
                val newIntersections =
                    if (left !in pipelinePoints && tracePoint in pipelinePoints && right !in pipelinePoints) intersections + tracePoint else intersections
                val newContainedPoints =
                    if (newIntersections.size % 2 == 1) containedPoints + tracePoint else containedPoints
                debug { "intersections up until now: $newIntersections" }
                debug { "containedPoints up until now: ${newContainedPoints - pipelinePoints}" }
                TraceAccumulator(newIntersections.filterNotNull().toSet(), newContainedPoints.filterNotNull().toSet())
            }.containedPoints - pipelinePoints
        }.toSet()

    private data class TraceAccumulator(
        val intersections: Set<Point> = emptySet(),
        val containedPoints: Set<Point> = emptySet()
    )

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