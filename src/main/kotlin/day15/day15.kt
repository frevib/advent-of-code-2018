package day15
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


fun main() {
    val input = readFile("src/main/resources/day15.txt")
    star1(input, 3)
    star2(input)
}

fun readFile(filePath: String): Array<String> {
    val fileInput = mutableListOf<String>()
    File(filePath).forEachLine { it -> fileInput.add(it) }
    return fileInput.toTypedArray()
}

enum class CharacterType {
    G, E
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

private class Node(
        var position: Pair<Int, Int>,
        var steps: Int,
        var path: List<Pair<Int, Int>> = ArrayList()
)

class FindPathResult(val path: List<Pair<Int, Int>>, val enemy: Character?, val direction: Direction)

class Character(var position: Pair<Int, Int>, var type: CharacterType, var attackPower: Int) {

    var hitPoints = 200

    fun attack(enemy: Character) {
        enemy.hitPoints -= attackPower
    }

    fun findEnemyLowestHP(enemies: List<Character>): Character? {
        var enemyLowestHP: Character? = null
        var lowestHitPoints = 201

        for (direction in Direction.values()) {
            for (enemy in enemies) {

                if (this.position + direction.getCoords() == enemy.position) {
                    if (enemy.hitPoints < lowestHitPoints) {
                        lowestHitPoints = enemy.hitPoints
                        enemyLowestHP = enemy
                    }
                }
            }
        }
        return enemyLowestHP
    }

    fun findPath(enemies: List<Character>, area: Array<Array<Char>>, otherAllies: List<Character>): MutableList<FindPathResult> {

        val areaCopy = area.map { it.clone() }.toTypedArray()
        val queue: Queue<Node> = LinkedList<Node>()
        val explored = mutableListOf<Node>()

        queue.add(Node(this.position, 0))

        val shortestPaths = mutableListOf<FindPathResult>()
        var shortestPathSteps = 200
        var shortestPathFound = false

        while (queue.isNotEmpty()) {

            val current: Node = queue.remove()

            if (shortestPathFound && (current.steps > shortestPathSteps)) {
                return shortestPaths
            }

            for (enemy in enemies) {
                for (direction in Direction.values()) {

                    if (enemy.position + direction.getCoords() == current.position) {
                        shortestPathSteps = current.steps

                        shortestPaths.add(FindPathResult(current.path, enemy, direction))
                        shortestPathFound = true
                    }
                }
            }

            // mark explored
            areaCopy[current.position.first][current.position.second] = 'x'

            // add children to queue
            if (isValidMove(current.position, Direction.UP, otherAllies, areaCopy)) {
                queue.add(Node(current.position + Direction.UP.getCoords(), current.steps + 1, current.path + arrayListOf(current.position + Direction.UP.getCoords())))
            }

            if (isValidMove(current.position, Direction.LEFT, otherAllies, areaCopy)) {
                queue.add(Node(current.position + Direction.LEFT.getCoords(), current.steps + 1, current.path + arrayListOf(current.position + Direction.LEFT.getCoords())))
            }

            if (isValidMove(current.position, Direction.RIGHT, otherAllies, areaCopy)) {
                queue.add(Node(current.position + Direction.RIGHT.getCoords(), current.steps + 1, current.path + arrayListOf(current.position + Direction.RIGHT.getCoords())))
            }

            if (isValidMove(current.position, Direction.DOWN, otherAllies, areaCopy)) {
                queue.add(Node(current.position + Direction.DOWN.getCoords(), current.steps + 1, current.path + arrayListOf(current.position + Direction.DOWN.getCoords())))
            }

            // keep track of explored nodes
            explored.add(current)

        }

        return mutableListOf(FindPathResult(listOf(Pair(-1, -1)), null, Direction.DOWN))
    }

    private fun isValidMove(closestEnemyPosition: Pair<Int, Int>, direction: Direction, otherAllies: List<Character>, area: Array<Array<Char>>): Boolean {
        return when (direction) {
            Direction.RIGHT -> isValidXMove(closestEnemyPosition, direction, otherAllies,  area)
            Direction.LEFT -> isValidXMove(closestEnemyPosition, direction, otherAllies, area)

            Direction.UP -> isValidYMove(closestEnemyPosition, direction, otherAllies, area)
            Direction.DOWN -> isValidYMove(closestEnemyPosition, direction, otherAllies, area)
        }
    }

    private fun isValidXMove(position: Pair<Int, Int>, direction: Direction, otherAllies: List<Character>, area: Array<Array<Char>>): Boolean {

        return (position.first + direction.getCoords().first < area[0].size)
                && isAtPositionOtherCharacter(position + direction.getCoords(), otherAllies)
                && isDot(position + direction.getCoords(), area)
    }

    private fun isValidYMove(position: Pair<Int, Int>, direction: Direction, otherAllies: List<Character>, area: Array<Array<Char>>): Boolean {

        return (position.second + direction.getCoords().second < area.size)
                && isAtPositionOtherCharacter(position + direction.getCoords(), otherAllies)
                && isDot(position + direction.getCoords(), area)
    }

    fun isAtPositionOtherCharacter(position: Pair<Int, Int>, otherAllies: List<Character>): Boolean {
        for (ally in otherAllies) {
            if (position == ally.position) {
                return false
            }
        }
        return true
    }

    private fun isDot(location: Pair<Int, Int>, area: Array<Array<Char>>): Boolean {
        return area[location.first][location.second] == '.'
    }

}

fun star1(input: Array<String>, elfAttackPower: Int): Int {

    // create area
    val area = Array(input[0].length) { Array(input.size) { 'x' } }

    // fill area and create units
    val goblins = mutableListOf<Character>()
    val elfs = mutableListOf<Character>()

    input.withIndex().forEach { (indexLine, line) ->
        line.withIndex().forEach { (indexItem, item) ->
            when (item) {
                'G' -> {
                    goblins.add(Character(Pair(indexItem, indexLine), CharacterType.G, 3))
                    area[indexItem][indexLine] = '.'
                }
                'E' -> {
                    elfs.add(Character(Pair(indexItem, indexLine), CharacterType.E, elfAttackPower))
                    area[indexItem][indexLine] = '.'
                }
                else -> {
                    area[indexItem][indexLine] = item
                }
            }
        }
    }


    // keep track of dead units
    val deadCharacters = mutableListOf<Character>()

    // game loop
    for (round in 0..100) {
        // Sort all characters according to reading order
        val allCharactersSorted = goblins
                .union(elfs)
                .sortedWith(compareBy({it.position.second}, {it.position.first})) // sort reading order
                .toMutableList()



        // Per character, handle movement
        loop@ for (character in allCharactersSorted) {

            // skip dead units
            if (character in deadCharacters) {
                continue
            }

            val allies = allCharactersSorted
                    .filterNot { it == character } // exclude itself
                    .filter { it.type == character.type } // only allies
                    .filterNot { it -> it in deadCharacters } // skip dead units

            val enemies = allCharactersSorted
                    .filterNot { it -> it.type == character.type } // only its enemies
                    .filterNot { it -> it in deadCharacters } // skip dead units
                    .toMutableList()


            if (enemies.size == 0) {
                val totalHitPoints = allCharactersSorted.filterNot { it -> it in deadCharacters }.map { it -> it.hitPoints }.sum()

                println("game ended at round: $round, total hit points: $totalHitPoints. Total hit points * round: ${round*totalHitPoints}")
//                printArea(area, allCharactersSorted.filterNot { it -> it in deadCharacters })
//                printHitPoints(allCharactersSorted.filterNot { it -> it in deadCharacters })

                return allCharactersSorted
                        .filter { it -> it.type == CharacterType.E }
                        .filterNot { it -> it in deadCharacters }
                        .count()
            }

            val findPathResults = character.findPath(enemies, area, allies)

            // if more that one shortest path, sort first step by reading order
            // check current position, then check positions of tiebreaker paths
            val readingOrderPath = findPathResults
                    .sortedWith(compareBy({(it.enemy!!.position + it.direction.getCoords()).second}, {(it.enemy!!.position + it.direction.getCoords()).first}))
                    .first()

            when {
                readingOrderPath.enemy == null -> {
                    continue@loop
                }
                readingOrderPath.path.isEmpty() -> {
                    // find with lowest health and attack that enemy
                    val enemyLowestHP = character.findEnemyLowestHP(enemies) ?: continue@loop
                    character.attack(enemyLowestHP)

                    // remove enemy if hp becomes lower than 1
                    if (enemyLowestHP.hitPoints < 1) {
                        deadCharacters.add(enemyLowestHP)
                    }
                }
                readingOrderPath.path.size == 1 -> {
                    // move
                    character.position = readingOrderPath.path[0]

                    // find with lowest health and attack that enemy
                    val enemyLowestHP = character.findEnemyLowestHP(enemies) ?: continue@loop
                    character.attack(enemyLowestHP)

                    // remove enemy if hp becomes lower than 1
                    if (enemyLowestHP.hitPoints < 1) {
                        deadCharacters.add(enemyLowestHP)
                    }
                }
                else ->  {
                    // move only
                    character.position = readingOrderPath.path[0]
                }
            }
        }

        println("round: $round")
    }

    // this shouldn't happen, just return some number
    return 1337
}


fun printArea(area: Array<Array<Char>>, allCharactersSorted: List<Character>) {
    // print area
    for (x in area.indices) {
        for (y in area[0].indices) {
            val character = allCharactersSorted.filter { it -> Pair(y, x) == it.position }
            if (character.isNotEmpty()) {
                print(character.single().type)
            } else {
                print(area[y][x])
            }

        }
        println("")
    }
}


fun star2(input: Array<String>) {

    val elvesCount = input
            .map { line -> line.count {char -> char == 'E'} }
            .sum()
    var attackPower = 4

    while (true) {
        val elvesLeft = star1(input, attackPower)
        println("elves left: $elvesLeft")

        if (elvesLeft == elvesCount) {
            println("with attack power $attackPower, elves will live")
            break
        }
        attackPower++
    }

}

fun printHitPoints(allCharactersSorted: List<Character>) {
    allCharactersSorted.forEach { println("${it.type}: HP: ${it.hitPoints}") }
}

private operator fun Pair<Int, Int>.plus(otherPair: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + otherPair.first, this.second + otherPair.second)
}

private operator fun Pair<Int, Int>.minus(otherPair: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first - otherPair.first, this.second - otherPair.second)
}
