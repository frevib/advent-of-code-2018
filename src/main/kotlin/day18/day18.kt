package day18

import java.io.File


fun main() {
    val inputLines = readFile("src/main/resources/day18.txt")

    star1(inputLines, 10, "star1")
    star2(inputLines, 1000, "star2")

}

fun readFile(filePath: String): Array<String> {
    val inputLines = mutableListOf<String>()
    File(filePath).forEachLine { line -> inputLines.add(line) }

    return inputLines.toTypedArray()
}

fun star1(input: Array<String>, iterations: Int, star: String) {

    // make 2d area
    var area = Array(input[0].length) {Array(input.size){ '.' }}
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            area[y][x] = char
        }
    }

    val elementCount = mutableMapOf<String, Int>()

    // iterate over each cell
    (1..iterations).forEach { iteration ->

        val areaCopy = area.map { it.clone() }.toTypedArray()
        area.forEachIndexed { x, array ->
            array.forEachIndexed { y, _ ->

                // check adjacent items for each cell
                val adjacents = adjacents(Pair(x, y), area)
                when (area[x][y]) {
                    '.' -> { // if two adjacent empty, transform to tree
                        if (adjacents.trees > 2) {
                            areaCopy[x][y] = '|'
                        }
                    }
                    '|' -> { // if two adjacent trees, transform to lumberyard
                        if (adjacents.lumberyard > 2) {
                            areaCopy[x][y] = '#'
                        }
                    }
                    '#' -> { // if at least one tree and one lumberyard, keep lumberyard
                        if (adjacents.trees > 0 && adjacents.lumberyard > 0) {

                        } else { // if not, empty cell
                            areaCopy[x][y] = '.'
                        }
                    }
                }
            }
        }

        area = areaCopy

        // find pattern in cycles (cycle detection) by counting all | and #
        val wood =  area.map { y -> y.count { element -> element == '|' } }.sum()
        val lumberyard =  area.map { y -> y.count { element -> element == '#' } }.sum()

        if (elementCount.containsKey(wood.toString() + lumberyard.toString())) {
//            println("same total at iteration: ${elementCount[(wood.toString()+lumberyard.toString())]}")
//            println("difference in iterations: ${iteration - elementCount[(wood.toString()+lumberyard.toString())]!!}")
        }

        elementCount[wood.toString() + lumberyard.toString()] = iteration

    }

//    printArea(area)
    val wood =  area.map { y -> y.count { element -> element == '|' } }.sum()
    val lumberyard =  area.map { y -> y.count { element -> element == '#' } }.sum()
    val answerStar1 = wood * lumberyard

    println("answer $star: $answerStar1")

}


class AdjacentAcres(var empty: Int, var trees: Int, var lumberyard: Int)

fun adjacents(position: Pair<Int, Int>, area: Array<Array<Char>>): AdjacentAcres {

    val adjacentAcres = AdjacentAcres(0, 0, 0)
    (-1..1).forEach { x ->
        for (it in (-1..1)
                .asSequence()
                .map { position + Pair(x, it) }) {

            // skip invalid boundaries like -1 and don't count itself as adjacent
            if (it.first < 0 ||
                    it.first > area[0].size -1 ||
                    it.second < 0 ||
                    it.second > area.size -1 ||
                    it == position) {
                continue
            }
            when {
                area[it.first][it.second] == '|' -> {
                    adjacentAcres.trees++
                }
                area[it.first][it.second] == '#' -> {
                    adjacentAcres.lumberyard++
                }
            }
        }
    }

    adjacentAcres.empty = 9 - adjacentAcres.trees - adjacentAcres.lumberyard
    return adjacentAcres

}

fun star2(input: Array<String>, iterations: Int, star: String) {
    star1(input, iterations, star)
}

private operator fun Pair<Int, Int>.plus(otherPair: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + otherPair.first, this.second + otherPair.second)
}

fun printArea(area: Array<Array<Char>>) {
    area.forEachIndexed { x, array ->
        array.forEachIndexed { y, element ->
            print(area[x][y])
        }
        println("")
    }
}
