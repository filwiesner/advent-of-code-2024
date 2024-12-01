package day01

import kotlin.math.abs
import println
import readInput

fun main() {
    fun parseLists(lines: List<String>): Pair<List<Int>, List<Int>> {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()

        for (line in lines.map(String::trim)) {
            val (leftId, rightId) = line.split("   ").map(String::toInt)
            left.add(leftId)
            right.add(rightId)
        }

        return left to right
    }

    fun part1(input: List<String>): Int {
        val (leftList, rightList) = parseLists(input)

        return leftList.sorted()
            .zip(rightList.sorted())
            .sumOf { (left, right) -> abs(left - right) }
    }

    fun part2(input: List<String>): Int {
        val (leftList, rightList) = parseLists(input)

        return leftList.sumOf { left ->
            val occurrences = rightList.count { right -> left == right }
            left * occurrences
        }
    }

    val testInput = example.lines()
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("day01/input")
    part1(input).println()
    part2(input).println()
}

val example = """
    3   4
    4   3
    2   5
    1   3
    3   9
    3   3
""".trimIndent()