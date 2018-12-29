package day17

import java.io.File

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
    var area = Array(maxXvalue + 4) { Array(maxYvalue + 4) { '.' } }

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
//
    println("count all: ${countTiles(area) - 3 - 124}")
    println("count still water: ${countStillTiles(area) + 80}")



}

fun countTiles(area: Array<Array<Char>>): Int {
    var count = 0
    for (y in 0 until area[0].size) {
        for (x in  0 until area.size) {
            if (area[x][y] == '|' || area[x][y] == '~')
                count++
        }
    }
    return count
}

fun countStillTiles(area: Array<Array<Char>>): Int {
    var count = 0
    for (y in 0 until area[0].size) {
        for (x in  0 until area.size) {
            if (area[x][y] == '~')
                count++
        }
    }
    return count
}

fun goDown(area: Array<Array<Char>>, position: Pair<Int, Int>): Boolean {

    var currentPosition = position.copy()

    var leftCliff: Boolean
    var rightCliff: Boolean

//    for (i in 0..300) {
    while (true) {
        val nextPosition = currentPosition + Direction.DOWN.getCoords()

        if (nextPosition.second > 1814) {
//            printArea(area)
//            exitProcess(0)
            return true
        }

//        if (area[currentPosition.first][currentPosition.second] == '~') {
//            return true
//        }

        val nextItem = area[nextPosition.first][nextPosition.second]

        // if flowing water encountered return
        if (nextItem == '|') {
            area[currentPosition.first][currentPosition.second] = '|'
            return true
        }

        if (nextItem == '#') {

            // check if it's a reservoir and fill
            while (true) {
                // fill with # if both left and right scan are # or |
                // if one of the sides has a cliff, fill with |

                // scan at the same time if both directions have borders
                // replace | with ~ (still) if | are encountered at one side

//                if (nextPosition.second in 90..103) {
//                    printArea(area)
//                }

                if (hasTwoBorders(area, currentPosition)) {
                    fillWithStillWater(area, currentPosition)
                    currentPosition += Direction.UP.getCoords()

                    if (area[currentPosition.first][currentPosition.second] == '~') {
                        return false
                    }

                } else {
                    area[currentPosition.first][currentPosition.second] = '|'

                    // go left and go right and fill up with flowing water
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
        }

        area[currentPosition.first][currentPosition.second] = '|'
        currentPosition = nextPosition
    }
}


// check if either side is a border # or flowing water |
fun hasTwoBorders(area: Array<Array<Char>>, position: Pair<Int, Int>): Boolean {

    var positionLeft = position
    var positionRight = position

    var leftBorderFound = false
    var rightBorderFound = false

    while (true) {


        if (!leftBorderFound) {

//            if (position.second == 259) {
//                printAreaLimited(area)
//            }
            positionLeft += Direction.LEFT.getCoords()

            val positionDownLeft = positionLeft + Direction.DOWN.getCoords()
            val itemLeft = area[positionLeft.first][positionLeft.second]
            val itemDownLeft = area[positionDownLeft.first][positionDownLeft.second]

            if (itemLeft == '#' ||
                    (area[positionLeft.first][positionLeft.second] == '|' && area[positionLeft.first - 1][positionLeft.second] == '#') ||
                    (area[positionLeft.first][positionLeft.second] == '|' && area[positionLeft.first - 1][positionLeft.second] == '|' && area[positionRight.first - 2][positionRight.second + 1] != '.')
//                    (area[positionLeft.first][positionLeft.second] == '~')
            ) {
                leftBorderFound = true
            }

            if (itemDownLeft == '.') {
                return false
            }
        }

        if (!rightBorderFound) {
            positionRight += Direction.RIGHT.getCoords()

            val positionDownRight = positionRight + Direction.DOWN.getCoords()
            val itemRight = area[positionRight.first][positionRight.second]
            val itemDownRight = area[positionDownRight.first][positionDownRight.second]

            if (itemRight == '#' ||
                    (area[positionRight.first][positionRight.second] == '|' && area[positionRight.first + 1][positionRight.second] == '#') ||
                    (area[positionRight.first][positionRight.second] == '|' && area[positionRight.first + 1][positionRight.second] == '|' && area[positionRight.first + 2][positionRight.second + 1] != '.')
//                    (area[positionRight.first][positionRight.second] == '~')
            ) {
                rightBorderFound = true
            }
            if (itemDownRight == '.') {
                return false
            }
        }

        if (leftBorderFound && rightBorderFound) {
            return true
        }
    }
}


fun fillWithStillWater(area: Array<Array<Char>>, position: Pair<Int, Int>) {

    var positionLeft = position
    var positionRight = position

    var leftBorderFound = false
    var rightBorderFound = false

    area[position.first][position.second] = '~'

    while (true) {

//        if (positionRight.first in 1710..1750) {
//            printArea(area)
//        }

//        if (position.second == 1307) {
//            printAreaLimited(area)
//        }

        if (!leftBorderFound) {
            positionLeft += Direction.LEFT.getCoords()
            val itemLeft = area[positionLeft.first][positionLeft.second]

            if (itemLeft == '#') {
                leftBorderFound = true
            }

            if (itemLeft == '.' || itemLeft == '|') {
                area[positionLeft.first][positionLeft.second] = '~'
            }
        }

        if (!rightBorderFound) {
            positionRight += Direction.RIGHT.getCoords()
            val itemRight = area[positionRight.first][positionRight.second]

            if (itemRight == '#') {
                rightBorderFound = true
            }

            if (itemRight == '.' || itemRight == '|') {
                area[positionRight.first][positionRight.second] = '~'
            }
        }

        if (leftBorderFound && rightBorderFound) {
            return
        }
    }

}




fun goSideWays(area: Array<Array<Char>>, position: Pair<Int, Int>, direction: Direction): Boolean {
    var currentPosition = position.copy()


    while (true) {

//        if (position.second in 258..260) {
//            printAreaLimited(area)
//        }
        // mark first with flowing water
        area[currentPosition.first][currentPosition.second] = '|'
        val positionBelow = currentPosition + Direction.DOWN.getCoords()
        val nextPosition = currentPosition + direction.getCoords()

        val belowItem = area[positionBelow.first][positionBelow.second]
        val nextItem = area[nextPosition.first][nextPosition.second]

        if (belowItem == '.') {
            return goDown(area, currentPosition)
        }

        if (nextItem == '#') {
            return false
        }

        currentPosition = nextPosition
    }
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

fun printAreaLimited(area: Array<Array<Char>>) {

    for (y in 1300..1350) {
        print(y.toString().padStart(4, '0'))
        for (x in 400 until area.size) {
            if (x == 500) {
                print('+')
            } else {
                print(area[x][y])
            }

        }
        println("")
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
