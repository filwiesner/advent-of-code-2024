package day05

import println
import readInput

private class UpdateComparator(val rules: List<Pair<Int, Int>>) : Comparator<Int> {
    override fun compare(a: Int?, b: Int?): Int {
        for ((ruleA, ruleB) in rules) {
            if (ruleA == a && ruleB == b) return -1
            if (ruleA == b && ruleB == a) return 1
        }
        return 0
    }
}

fun main() {
    fun parseRules(lines: List<String>): List<Pair<Int, Int>> = lines.map { line ->
        val (a, b) = line.trim().split("|").map(String::toInt)
        a to b
    }

    fun parseUpdates(lines: List<String>): List<List<Int>> = lines
        .map { line -> line.trim().split(",").map(String::toInt) }


    fun part1(input: List<String>): Int {
        val rules = parseRules(input.takeWhile { line -> line.isNotBlank() })
        val updates = parseUpdates(input.slice((rules.size + 1)..input.lastIndex))
        val updateComparator = UpdateComparator(rules)

        val numbers = mutableListOf<Int>()
        for (update in updates) {
            val sortedUpdate = update.sortedWith(updateComparator)
            if (sortedUpdate == update)
                numbers.add(update[update.lastIndex / 2])
        }

        return numbers.sum()
    }

    fun part2(input: List<String>): Int {
        val rules = parseRules(input.takeWhile { line -> line.isNotBlank() })
        val updates = parseUpdates(input.slice((rules.size + 1)..input.lastIndex))
        val updateComparator = UpdateComparator(rules)

        val numbers = mutableListOf<Int>()
        for (update in updates) {
            val sortedUpdate = update.sortedWith(updateComparator)
            if (sortedUpdate != update)
                numbers.add(sortedUpdate[sortedUpdate.lastIndex / 2])
        }

        return numbers.sum()
    }

    val testInput = example.lines()
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput("day05/input")
    part1(input).println()
    part2(input).println()
}

val example = """
    47|53
    97|13
    97|61
    97|47
    75|29
    61|13
    75|53
    29|13
    97|29
    53|29
    61|53
    97|53
    61|29
    47|13
    75|47
    97|75
    47|61
    75|61
    47|29
    75|13
    53|13

    75,47,61,53,29
    97,61,53,29,13
    75,29,13
    75,97,47,61,53
    61,13,29
    97,13,75,29,47
""".trimIndent()