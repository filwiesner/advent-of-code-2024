import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs
import kotlin.math.hypot

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun <T> T.println() = this.also { println(this) }

fun <T> ArrayDeque<T>.removeFirstWhile(condition: (T) -> Boolean): List<T> {
    val removedElements = mutableListOf<T>()
    while (isNotEmpty() && condition(first())) {
        removedElements.add(removeFirst())
    }
    return removedElements
}

fun gcd(a: Long, b: Long): Long = if (a == 0L) b else gcd(b % a, a)
fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

fun lcm(numbers: List<Int>): Long {
    var result = 1L

    for (i in numbers.indices) {
        result = lcm(numbers[i].toLong(), result)
        if (result == 0L) return result
    }

    return result
}

enum class Direction(val offset: IntOffset) {
    Up(IntOffset(0, -1)),
    UpRight(IntOffset(1, -1)),
    Right(IntOffset(1, 0)),
    DownRight(IntOffset(1, 1)),
    Down(IntOffset(0, 1)),
    DownLeft(IntOffset(-1, 1)),
    Left(IntOffset(-1, 0)),
    UpLeft(IntOffset(-1, -1))
}

data class IntOffset(val x: Int, val y: Int)
data class LongOffset(val x: Long, val y: Long)

val IntOffset.inverted get() = IntOffset(y, x)
val IntOffset.isPositive get() = x >= 0 && y >= 0

operator fun IntOffset.plus(offset: IntOffset) = IntOffset(x + offset.x, y + offset.y)
operator fun IntOffset.minus(offset: IntOffset) = IntOffset(x - offset.x, y - offset.y)
operator fun IntOffset.times(multiplier: Int) = IntOffset(x * multiplier, y * multiplier)
fun IntOffset.distanceTo(offset: IntOffset): Int = hypot(offset.x - x.toFloat(), offset.y - y.toFloat()).toInt()
fun IntOffset.isStraightLineTo(offset: IntOffset): Boolean {
    val isDiagonal = with(this - offset) { abs(x) == abs(y) }
    val isVerticalOrHorizontal = x == offset.x || y == offset.y
    return isDiagonal || isVerticalOrHorizontal
}

fun IntOffset.directionTo(offset: IntOffset): Direction {
    val directionVector = with(offset - this) {
        IntOffset(x.coerceIn(-1..1), y.coerceIn(-1..1))
    }
    return Direction.entries.first { direction -> direction.offset == directionVector }
}
operator fun IntOffset.plus(direction: Direction) = this + direction.offset
fun IntOffset.getDiagonal(direction: IntOffset) = sequence {
    var current = this@getDiagonal
    while (true) {
        yield(current)
        current += direction
    }
}

fun extractGridLine(start: IntOffset, end: IntOffset, matrix: List<List<Char>>): String {
    require(start.isStraightLineTo(end)) { "Line $start - $end is not straight" }
    if (start == end) return ""

    val direction = start.directionTo(end).offset
    return start
        .getDiagonal(direction)
        .takeWhile { offset -> offset - direction != end } // take while the last coord is not the last
        .mapNotNull { offset -> matrix.getOrNull(offset.y)?.getOrNull(offset.x) }
        .joinToString(separator = "")
}

fun List<Direction>.calculatePosition() = fold(IntOffset(0, 0)) { acc, dir -> acc + dir.offset }

fun <T> List<T>.getCombinations(): List<Pair<T, T>> {
    val combinations = mutableListOf<Pair<T, T>>()
    for (i in indices) {
        for (j in i + 1 until size) combinations.add(Pair(this[i], this[j]))
    }
    return combinations
}