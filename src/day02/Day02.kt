package day02

import kotlin.math.abs
import println
import readInput

fun main() {
    fun parseInput(input: List<String>): List<List<Int>> = input.map { levels -> levels.split(" ").map(String::toInt) }
    fun List<Int>.isSafe(): Boolean {
        var lastLevel = this[0]
        var reportIncreases = lastLevel < this[1]

        for (level in this.drop(1)) {
            val isValidDifference = abs(level - lastLevel) in 1..3
            val isValidDirection = reportIncreases == (lastLevel < level)
            if (!(isValidDifference && isValidDirection)) return false

            lastLevel = level
        }

        return true
    }

    fun part1(input: List<String>): Int = parseInput(input)
        .map { report -> report.isSafe() }
        .count { isSafe -> isSafe }

    fun part2(input: List<String>): Int = parseInput(input)
        .map { report ->
            if (report.isSafe()) return@map true
            return@map report.indices.any { removedReport ->
                val dampenedReport = report.toMutableList().apply { removeAt(removedReport) }
                dampenedReport.isSafe()
            }
        }
        .count { isSafe -> isSafe }

    val testInput = example.lines()
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("day02/input")
    part1(input).println()
    part2(input).println()
}

val example = """
    7 6 4 2 1
    1 2 7 8 9
    9 7 6 2 1
    1 3 2 4 5
    8 6 4 4 1
    1 3 6 7 9
""".trimIndent()