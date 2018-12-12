package day10

import java.io.File
import kotlin.math.absoluteValue

fun main() {
    val file = readFile("src/main/resources/day10.txt")

    star1(file)
}

fun readFile(fileName: String): Array<String> {
    val fileArray: ArrayList<String> = ArrayList()
    File(fileName).forEachLine {
        fileArray.add(it)
    }
    return fileArray.toTypedArray()
}


fun star1(lines: Array<String>) {
    var coords = lines
            .map { it -> Pair(
                    it.substring(it.indexOf("<") + 1, it.indexOf(",")).trim().toInt(),
                    it.substring(it.indexOf(",") + 1, it.indexOf(">")).trim().toInt()
            )}

    val velocity = lines
            .map { it -> Pair(
                    it.substring(it.indexOf("<", 27) + 1, it.indexOf(",", 27)).trim().toInt(),
                    it.substring(it.indexOf(",", 27) + 1, it.indexOf(">", 27)).trim().toInt()
            )}

    var seconds = 0
    while (true) {
        seconds++

        val lastMinSquare = (coords.minBy { it -> it.first }!!.first - coords.maxBy { it -> it.first }!!.first).absoluteValue *
                (coords.minBy { it -> it.second }!!.second - coords.maxBy { it -> it.second }!!.second).absoluteValue

        coords = coords.zip(velocity).map { it -> it.first.plus(it.second) }
        val minSquare = (coords.minBy { it -> it.first }!!.first - coords.maxBy { it -> it.first }!!.first).absoluteValue *
                (coords.minBy { it -> it.second }!!.second - coords.maxBy { it -> it.second }!!.second).absoluteValue


        if ((lastMinSquare < minSquare) &&
                (coords.minBy { it -> it.second }!!.second - coords.maxBy { it -> it.second }!!.second).absoluteValue < 400) {
            coords = coords.zip(velocity).map { it -> it.first.minus(it.second) }
            println("Answer second star: $seconds")
            break
        }


    }


    println("Answer first star:")
    for (x in coords.minBy { it.second }!!.second..coords.maxBy { it.second }!!.second) {
        for (y in coords.minBy { it.first }!!.first..coords.maxBy { it.first }!!.first) {
            if (containsCoordinate(x, y, coords)) {
                print("#")
            } else {
                print(".")
            }
        }
        println("")
    }

}

fun containsCoordinate(x: Int, y: Int, coords: List<Pair<Int, Int>>): Boolean {
    for (pair in coords) {
        if (pair.first == y && pair.second == x) {
            return true
        }
    }

    return false
}

private operator fun Pair<Int, Int>.plus(otherPair: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + otherPair.first, this.second + otherPair.second)
}

private operator fun Pair<Int, Int>.minus(otherPair: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first - otherPair.first, this.second - otherPair.second)
}
