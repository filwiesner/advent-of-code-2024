package day04

import Direction
import IntOffset
import extractGridLine
import isPositive
import plus
import println
import readInput
import times

fun main() {
    fun findAllLetterCoordinates(letter: Char, matrix: List<List<Char>>): List<IntOffset> = buildList {
        for ((y, line) in matrix.withIndex()) {
            for ((x, char) in line.withIndex()) {
                if (char != letter) continue
                add(IntOffset(x, y))
            }
        }
    }

    fun part1(input: List<String>): Int {
        var xmasCount = 0
        val matrix = input.map(String::toList)
        for (coordinates in findAllLetterCoordinates('X', matrix)) {
            val words = Direction.entries
                .map { direction -> coordinates.plus(direction.offset * 3) } // 3 for 3 letters MAS
                .filter(IntOffset::isPositive)
                .map { end -> extractGridLine(coordinates, end, matrix) }

            xmasCount += words.count { string -> string == "XMAS" }
        }

        return xmasCount
    }

    fun part2(input: List<String>): Int {
        var xmasCount = 0
        val matrix = input.map(String::toList)
        for (coordinates in findAllLetterCoordinates('A', matrix)) {
            val topDown = extractGridLine(coordinates + Direction.UpLeft, coordinates + Direction.DownRight, matrix)
            val bottomUp = extractGridLine(coordinates + Direction.DownLeft, coordinates + Direction.UpRight, matrix)

            val options = listOf("SAM", "MAS")
            if (topDown in options && bottomUp in options) {
                xmasCount += 1
            }
        }

        return xmasCount
    }

    val testInput = example.lines()
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("day04/input")
    part1(input).println()
    part2(input).println()
}

val example = """
    MMMSXXMASM
    MSAMXMSMSA
    AMXSXMAAMM
    MSAMASMSMX
    XMASAMXAMM
    XXAMMXXAMA
    SMSMSASXSS
    SAXAMASAAA
    MAMMMXMMMM
    MXMXAXMASX
""".trimIndent()