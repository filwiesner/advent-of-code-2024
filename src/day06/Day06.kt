package day06

import Direction
import IntOffset
import getPath
import println
import readInput

private val positions = mapOf(
    '^' to Direction.Up,
    '>' to Direction.Right,
    'v' to Direction.Down,
    '<' to Direction.Left,
)

private class GuardMap(
    val map: List<List<Char>>,
) {
    data class MoveResult(val path: List<IntOffset>, val exitedTheMap: Boolean)

    var guardPosition: IntOffset = map.flatten()
        .indexOfFirst { char -> positions.keys.contains(char) }
        .let { index ->
            val x = index % map.size
            val y = index / map.size
            IntOffset(x, y)
        }
    var guardDirection: Direction = positions[map[guardPosition.y][guardPosition.x]]!!
    val guardPath: MutableSet<IntOffset> = mutableSetOf()

    fun moveGuard(): MoveResult {
        val path = guardPosition
            .getPath(guardDirection.offset)
            .takeWhile { offset ->
                val symbol = map.getOrNull(offset.y)?.getOrNull(offset.x) ?: '.'
                symbol != '#'
            }
            .take(map.size + 1) // make sure we don't go endlessly
            .toList()

        if (!path.last().isInMap()) {
            val actualPath = path.filter { offset -> offset.isInMap() }
            guardPath.addAll(actualPath)
            return MoveResult(actualPath, true)
        }

        guardPosition = path.last()
        turn()
        guardPath.addAll(path)
        return MoveResult(path, false)
    }

    fun turn() {
        val newIndex = positions.values.indexOf(guardDirection) + 1
        guardDirection = positions.values.toList()[newIndex % positions.size]
    }

    fun IntOffset.isInMap() = x in 0..map.lastIndex && y in 0..map.lastIndex
}

fun main() {
    fun part1(input: List<String>): Int {
        val map = GuardMap(input.map(String::toList))
        var lastPath: GuardMap.MoveResult = GuardMap.MoveResult(mutableListOf(), false)

        while (lastPath.exitedTheMap == false) {
            lastPath = map.moveGuard()
        }

        return map.guardPath.size
    }

    fun part2(input: List<String>): Int {
        val startingMap = GuardMap(input.map(String::toList))
        val mapsWithObstacle = buildList {
            repeat(input.size) { y ->
                repeat(input.size) { x ->
                    if (IntOffset(x, y) == startingMap.guardPosition) return@repeat // skip removing guard
                    val lines = input.mapIndexed { index, line ->
                        if (index == y) line.replaceRange(x, x + 1, "#")
                        else line
                    }
                    add(GuardMap(lines.map(String::toList)))
                }
            }
        }

        val loopedMaps = mapsWithObstacle.filter { map ->
            var lastPath: GuardMap.MoveResult = map.moveGuard()
            var endPoints = mutableSetOf<IntOffset>()
            var moves = 0
            while (lastPath.exitedTheMap == false) {
                moves += 1
                lastPath = map.moveGuard()
                if (moves > input.size * input.size)
                    return@filter true // looping
                endPoints.add(lastPath.path.last())
            }
            false
        }

        return loopedMaps.size
    }

    val testInput = example.lines()
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("day06/input")
    part1(input).println()
    part2(input).println()
}

val example = """
    ....#.....
    .........#
    ..........
    ..#.......
    .......#..
    ..........
    .#..^.....
    ........#.
    #.........
    ......#...
""".trimIndent()