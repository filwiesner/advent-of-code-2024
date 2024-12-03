package day03

import println
import readInput

fun main() {
    fun sanitizeInput(input: String): String = Regex("""don't\(\).*?do\(\)|don't\(\).*?${'$'}""").replace(input, "")

    fun findMulExpressions(line: String): List<String> = Regex("""mul\(\d*,\d*\)""")
        .findAll(line)
        .map { match -> match.value }
        .toList()

    fun evaluateMul(expression: String): Int {
        val (a, b) = expression.drop(4).dropLast(1).split(",").map(String::toInt)
        return a * b
    }

    fun part1(input: String): Int =
        input
            .let(::findMulExpressions)
            .sumOf(::evaluateMul)

    fun part2(input: String): Int =
        input
            .let(::sanitizeInput)
            .let(::findMulExpressions)
            .sumOf(::evaluateMul)

    check(part1(example) == 161)
    check(part2(example2) == 48)

    val input = readInput("day03/input").first()
    part1(input).println()
    part2(input).println()
}

val example = """
    xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
""".trimIndent()
val example2 = """
    xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
""".trimIndent()