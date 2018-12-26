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
    val maxValue = inputLines
            .asSequence()
            .map { line ->
                val values =
                        Regex("(\\w=)(\\d{1,4})(,\\s)(\\w=)(\\d{1,4})(\\.\\.)(\\d{1,4})").find(line)!!.destructured
                listOf(values.component2(), values.component5(), values.component7())
            }
            .flatten()
            .map { number -> number.toInt() }
            .max()!!

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



    println("max X value: $maxValue")

    // make slice area
    var area = Array(maxYvalue + 1) { Array(maxXvalue + 1) { '.' } }

    // fill area with clay
    for (line in inputLines) {
        if (line.first() == 'x') {
            val x = Regex("x=(\\d{1,4})").find(line)!!.destructured.component1().toInt()
            val (y1, y2) = Regex("(\\d{1,4}\\.\\.\\d{1,4})").find(line)!!.value.split("..")
            for (y in y1.toInt()..y2.toInt()) {
                area[y][x] = '#'
            }
        } else if (line.first() == 'y') {
            val y = Regex("y=(\\d{1,4})").find(line)!!.destructured.component1().toInt()
            val (x1, x2) = Regex("(\\d{1,4}\\.\\.\\d{1,4})").find(line)!!.value.split("..")
            for (x in x1.toInt()..x2.toInt()) {
                area[y][x] = '#'
            }
        }
    }


    val waterSourcePosition = Pair(500, 0)

//    val newArea = goDown(area, waterSourcePosition)


    printArea(area)


}

fun goDown(area: Array<Array<Char>>, position: Pair<Int, Int>) {

    var currentPosition: Pair<Int, Int> = position.copy()

    for (i in 0..300) {
        currentPosition += Direction.DOWN.getCoords()

        if (currentPosition.second > 200) {
            printArea(area)
            exitProcess(0)
            return
        }

        if (area[currentPosition.first][currentPosition.second] == '#') {

            // check if it's a reservoir and fill
            for (i in 1..100) {
                currentPosition += Direction.UP.getCoords()

                val boundaryLeft = hasBoundary(area, currentPosition, Direction.LEFT)
                val boundaryRight = hasBoundary(area, currentPosition, Direction.RIGHT)

                if (boundaryLeft) {
                    // fill till boundary
                    fillLayer(area, currentPosition, Direction.LEFT)
                }

                if (boundaryRight) {
                    // fill till boundary
                    fillLayer(area, currentPosition, Direction.RIGHT)
                }

                if (!boundaryLeft || !boundaryRight) {
                    break
                }
            }

            goSideWays(area, currentPosition, Direction.LEFT)
            goSideWays(area, currentPosition, Direction.RIGHT)
            return
        }

        currentPosition = currentPosition
        area[currentPosition.first][currentPosition.second] = '|'

    }
}


fun hasBoundary(area: Array<Array<Char>>, position: Pair<Int, Int>, direction: Direction): Boolean {

    var nextPosition = position.copy()
    var downPosition = position.copy()
    for (i in 1..100) {
        nextPosition += direction.getCoords()
        downPosition = nextPosition + Direction.DOWN.getCoords()
        if (area[nextPosition.first][nextPosition.second] == '#') {
            return true
        }


        if (area[downPosition.first][downPosition.second] == '.') {
            return false
        }
    }
    return false
}

fun fillLayer(area: Array<Array<Char>>, position: Pair<Int, Int>, direction: Direction) {

    var nextPosition = position.copy()
    // check left
    for (i in 0..100) {
        nextPosition += direction.getCoords()
        if (area[nextPosition.first][nextPosition.second] != '#') {
            area[nextPosition.first][nextPosition.second] = '~'
        } else {
            return
        }
    }
}

fun goSideWays(area: Array<Array<Char>>, position: Pair<Int, Int>, direction: Direction) {
    var currentPosition = position.copy()

    for (i in 0..100) {

        val nextPosition = currentPosition + direction.getCoords()
        val positionBelow = currentPosition + Direction.DOWN.getCoords()

//        if (nextPosition.first > 1400) {
//            return
//        }

        if (area[nextPosition.first][nextPosition.second] == '#') {
            return
        }

        if (area[positionBelow.first][positionBelow.second] == '.') {
            goDown(area, currentPosition)
            return
        }

        currentPosition += direction.getCoords()
        area[currentPosition.first][currentPosition.second] = '|'
    }
    return
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

    for (y in 0 until area.size) {
        for (x in 400 until area[0].size) {
            print(area[y][x])
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
