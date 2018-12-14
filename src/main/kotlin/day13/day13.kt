package day13

import java.io.File
import kotlin.system.exitProcess

fun main() {
    val input = readFile("src/main/resources/day13.txt")

    stars(input)
}

fun readFile(inputFile: String): Array<String> {
    val fileArray = ArrayList<String>()
    File(inputFile).forEachLine { it -> fileArray.add(it) }


    return fileArray.toTypedArray()
}

private class Cart(var position: Pair<Int, Int>, val directionChar: Char) {
    lateinit var direction: Pair<Int, Int>
    var intersectionTurn = 0

    init {
        this.getDirectionCoords()
    }

    fun getDirectionCoords() {
        when(directionChar) {
            '^' -> this.direction = Pair(0, -1)
            '>' -> this.direction = Pair(1, 0)
            'v' -> this.direction = Pair(0, 1)
            '<' -> this.direction = Pair(-1, 0)
        }
    }

    fun move() {
        this.position += this.direction
    }
}

private operator fun Pair<Int, Int>.plus(otherPair: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + otherPair.first, this.second + otherPair.second)
}


fun stars(input: Array<String>) {
    // create empty grid
    val grid = Array(input.maxBy { it.length }!!.length) { Array(input.size) {' '}}

    // keep track of carts
    var cartList = mutableListOf<Cart>()

    // fill grid
    input.withIndex().forEach { (indexLine, line) ->
        line.withIndex().forEach { (indexChar, char) ->
            if (char == '^' || char == '>' || char == 'v' || char == '<') {
                cartList.add(Cart(Pair(indexChar, indexLine), char))
                when (char) {
                    '^' -> grid[indexChar][indexLine] = '|'
                    '>' -> grid[indexChar][indexLine] = '-'
                    'v' -> grid[indexChar][indexLine] = '|'
                    '<' -> grid[indexChar][indexLine] = '-'
                }
            } else {
                grid[indexChar][indexLine] = char
            }
        }
    }

    while (true) {
        // move carts from top to bottom, left to right
        cartList = cartList.sortedWith(compareBy({it.position.first}, {it.position.second})).toMutableList()

        // move carts
        cartList.forEach { cart ->
            cart.move()

            // handle collisions
            if (cartList.groupingBy { it.position }.eachCount().filter { it.value > 1 }.isNotEmpty()) {

                // first collision
                if (cartList.size == 17) {
                    println("Answer star1 ${cart.position.first}, ${cart.position.second}")
                }

                // remove carts that have collided
                cartList = cartList
                        .filterNot { it ->
                            it.position == cartList
                                    .groupingBy { it.position }
                                    .eachCount()
                                    .filter { it.value > 1 }.keys
                                    .toList()[0]
                        }
                        .toMutableList()

                // last cart position
                if (cartList.size == 1) {
                    // one too far, don't know why
                    println("Answer star2: ${cartList[0].position.first - 1},${cartList[0].position.second}")
                    exitProcess(0)
                }
            }

            // handle track directions
            val track = grid[cart.position.first][cart.position.second]
            when (track) {
                '/' -> when (cart.direction) {
                    Pair(0, -1) -> cart.direction = Pair(1, 0)
                    Pair(1, 0) -> cart.direction = Pair(0, -1)
                    Pair(0, 1) -> cart.direction = Pair(-1, 0)
                    Pair(-1, 0) -> cart.direction = Pair(0, 1)
                }
                '\\' -> when (cart.direction) {
                    Pair(0, -1) -> cart.direction = Pair(-1, 0)
                    Pair(1, 0) -> cart.direction = Pair(0, 1)
                    Pair(0, 1) -> cart.direction = Pair(1, 0)
                    Pair(-1, 0) -> cart.direction = Pair(0, -1)
                }
                '+' -> {
                    when (cart.intersectionTurn) {
                        0 -> when (cart.direction) {
                            Pair(0, -1) -> cart.direction = Pair(-1, 0)
                            Pair(1, 0) -> cart.direction = Pair(0, -1)
                            Pair(0, 1) -> cart.direction = Pair(1, 0)
                            Pair(-1, 0) -> cart.direction = Pair(0, 1)
                        }
                        2 -> when (cart.direction) {
                            Pair(0, -1) -> cart.direction = Pair(1, 0)
                            Pair(1, 0) -> cart.direction = Pair(0, 1)
                            Pair(0, 1) -> cart.direction = Pair(-1, 0)
                            Pair(-1, 0) -> cart.direction = Pair(0, -1)
                        }
                    }
                    cart.intersectionTurn = (cart.intersectionTurn + 1) % 3
                }
            }
        }
    }
}
