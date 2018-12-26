package day17

import java.io.File
import kotlin.system.exitProcess

fun main() {
    val inputLines = readFile("src/main/resources/day17.txt")

    star1(inputLines)
}

fun readFile(filePath: String): Array<String> {
    val inputLines = mutableListOf<String>()
    File(filePath).forEachLine { line -> inputLines.add(line) }

    return inputLines.toTypedArray()
}


fun star1(inputLines: Array<String>) {

    // max value needed for area size
    val maxXvalue = inputLines
            .map { line ->
                val xValues = Regex("(?:x=)(\\d{1,4})(?:\\.\\.)?(\\d{1,4})?").find(line)!!.destructured.toList()
                xValues
            }
            .flatten()
            .filter { item -> item.isNotEmpty() }
            .map { item -> item.toInt() }
            .max()!!

    val maxYvalue = inputLines
            .map { line ->
                val yValues = Regex("(?:y=)(\\d{1,4})(?:\\.\\.)?(\\d{1,4})?").find(line)!!.destructured.toList()
                yValues
            }
            .flatten()
            .filter { item -> item.isNotEmpty() }
            .map { item -> item.toInt() }
            .max()!!
    println("max X value: $maxXvalue")
    println("max Y value: $maxYvalue")


    // make slice area
    var area = Array(maxXvalue + 1) { Array(maxYvalue + 1) { '.' } }

    // fill area with clay
    for (line in inputLines) {
        if (line.first() == 'x') {
            val x = Regex("x=(\\d{1,4})").find(line)!!.destructured.component1().toInt()
            val (y1, y2) = Regex("(\\d{1,4}\\.\\.\\d{1,4})").find(line)!!.value.split("..")
            for (y in y1.toInt()..y2.toInt()) {
                area[x][y] = '#'
            }
        } else if (line.first() == 'y') {
            val y = Regex("y=(\\d{1,4})").find(line)!!.destructured.component1().toInt()
            val (x1, x2) = Regex("(\\d{1,4}\\.\\.\\d{1,4})").find(line)!!.value.split("..")
            for (x in x1.toInt()..x2.toInt()) {
                area[x][y] = '#'
            }
        }
    }

    val waterSourcePosition = Pair(500, 0)

    val newArea = goDown(area, waterSourcePosition)

    printArea(area)

}

fun goDown(area: Array<Array<Char>>, position: Pair<Int, Int>): Boolean {

    var currentPosition = position.copy()

    var leftCliff: Boolean
    var rightCliff: Boolean

//    for (i in 0..300) {
    while (true) {
        currentPosition += Direction.DOWN.getCoords()


        if (currentPosition.second > 400) {
//            printArea(area)
//            exitProcess(0)
            return true
        }

//        if (area[currentPosition.first][currentPosition.second] == '~') {
//            return true
//        }


        val currentItem = area[currentPosition.first][currentPosition.second]
        if (currentItem == '#' || currentItem == '~') {

            // check if it's a reservoir and fill
//            for (i in 1..300) {
            while (true) {
                currentPosition += Direction.UP.getCoords()

                area[currentPosition.first][currentPosition.second] = '|'

                // go left and go right and fill up

                leftCliff = goSideWays(area, currentPosition, Direction.LEFT)
                rightCliff = goSideWays(area, currentPosition, Direction.RIGHT)

                if (leftCliff || rightCliff) {
                    return true
                }

                if (currentPosition == Pair(500, 0)) {
                    return true
                }
            }
        }

        area[currentPosition.first][currentPosition.second] = '|'
    }

    // execution should not reach here
//    return true
}


fun goSideWays(area: Array<Array<Char>>, position: Pair<Int, Int>, direction: Direction): Boolean {
    var currentPosition = position.copy()

//    for (i in 0..300) {

    while (true) {
        currentPosition += direction.getCoords()
        val positionBelow = currentPosition + Direction.DOWN.getCoords()

//        if (nextPosition.first > 1400) {
//            return
//        }

        val currentItem = area[currentPosition.first][currentPosition.second]
        if (currentItem == '#') {
            return false
        }
//
        if (area[positionBelow.first][positionBelow.second] == '.') {
            return goDown(area, currentPosition)
        }

//        currentPosition += direction.getCoords()
        area[currentPosition.first][currentPosition.second] = '~'
    }

    // execution should not reach here
//    return true
}


enum class Direction(val direction: Pair<Int, Int>) {
    UP(Pair(0, -1)),
    LEFT(Pair(-1, 0)),
    RIGHT(Pair(1, 0)),
    DOWN(Pair(0, 1)),
    ;

    fun getCoords(): Pair<Int, Int> {
        return this.direction
    }

}

fun printArea(area: Array<Array<Char>>) {

    for (y in 0 until area[0].size) {
        print(y.toString().padStart(4, '0'))
        for (x in 400 until area.size) {
            print(area[x][y])
        }
        println("")
    }
}


private operator fun Pair<Int, Int>.plus(otherPair: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + otherPair.first, this.second + otherPair.second)
}

private operator fun Pair<Int, Int>.minus(otherPair: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first - otherPair.first, this.second - otherPair.second)
}
