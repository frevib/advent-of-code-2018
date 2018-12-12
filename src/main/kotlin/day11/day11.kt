package day11

fun main() {
    val input = 5719
    star1(input)
    star2(input)
}


fun star1(gridSerial: Int) {
    val grid = createGrid(gridSerial)
    var maxPowerSquare = 0
    var maxPowerSquareCoords = Pair(0,0)

        for (i in 0 until grid.size - 3) {
            for (j in 0 until  grid.size - 3) {
                var squarePower = 0
                for (x in 0 until 3) {
                    for (y in 0 until 3) {
                        squarePower += grid[i + x][j + y]
                    }
                }

                if (squarePower > maxPowerSquare) {
                    maxPowerSquareCoords = Pair(i, j)
                    maxPowerSquare = squarePower
                }
            }
        }


    println("Answer star 1: ${maxPowerSquareCoords.first},${maxPowerSquareCoords.second}")
}

fun star2(gridSerial: Int) {

    val grid = createGrid(gridSerial)
    var maxPowerSquare = 0
    var maxPowerSquareCoords = Pair(0,0)
    var maxPowerSquareSize = 0


    println("this can take a while.. 2-6 mins")
    for (squareSize in 1 until 300) {
        for (i in 0..grid.size - squareSize) {
            for (j in 0..grid.size - squareSize) {
                var squarePower = 0
                for (x in 0 until squareSize) {
                    for (y in 0 until squareSize) {
                        squarePower += grid[i + x][j + y]
                    }
                }

                if (squarePower > maxPowerSquare) {
                    maxPowerSquareCoords = Pair(i, j)
                    maxPowerSquare = squarePower
                    maxPowerSquareSize = squareSize
//                    println("im alive: $maxPowerSquareCoords, at: $squareSize")
                }
            }
        }

    }

    println("Answer star2: ${maxPowerSquareCoords.first},${maxPowerSquareCoords.second},$maxPowerSquareSize")
}

fun createGrid(gridSerial: Int): Array<Array<Int>> {
    val grid = Array(300) { Array(300) {0} }

    for (x in 0 until  grid.size) {
        for (y in 0 until  grid[0].size) {
            val rackId = x + 10
            val power = ((rackId * y) + gridSerial) * rackId
            val hundreds = ((power / 100) % 10) - 5
            grid[x][y] = hundreds
        }
    }
    return grid
}

