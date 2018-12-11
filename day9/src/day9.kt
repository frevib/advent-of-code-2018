import java.lang.RuntimeException
import java.util.*
import kotlin.collections.HashMap

class Marble {

    var previous: Marble = this
    var value: Long
    var next: Marble = this

    constructor(element: Long) {
        this.value = element
    }

    constructor(previous: Marble, element: Long, next: Marble) {
        this.previous = previous
        this.value = element
        this.next = next
    }

    fun insert(number: Long): Marble {
        val newNode = Marble(this, number, this.next)
        this.next.previous = newNode
        this.next = newNode
        return newNode

    }

    fun next(amount: Int): Marble {
        if (amount < 0) {
            throw RuntimeException("no negative number possible")
        }
        if (amount == 0) {
            return this
        }

        var marble: Marble = this
        for (i in 0 until amount) {
            marble = marble.next
        }
        return marble
    }

    fun previous(amount: Long): Marble {
        if (amount < 0) {
            throw RuntimeException("no negative number possible")
        }
        if (amount == 0L) {
            return this
        }

        var marble: Marble = this
        for (i in 0 until  amount) {
            marble = marble.previous
        }
        return marble
    }

    fun remove(): Marble {
        val previous = this.previous
        val next = this.next
        previous.next = next
        next.previous = previous
        return this.next
    }
}


fun main() {
    maxScore(479, 71035)
    maxScore(479, 71035 * 100)
}

fun maxScore(players: Int, lastMarble: Long) {
    val circle = LinkedList<Int>()
    circle.add(0)

    var currentMarble = Marble(0)
    val playerScores = HashMap<Long, Long>()

    for (i in 0L..players) {
        playerScores[i] = 0L
    }

    (1L..lastMarble).forEach { marbleNumber ->
        if (marbleNumber % 23L == 0L) {
            val marbleSum = playerScores[marbleNumber % players]!! + marbleNumber
            playerScores[marbleNumber % players] = marbleSum
            currentMarble = currentMarble.previous(7)

            val newValue = playerScores[marbleNumber % players]!! + currentMarble.value
            playerScores[marbleNumber % players] = newValue
            currentMarble = currentMarble.remove()

        } else {
            currentMarble = currentMarble.next(1)
            currentMarble = currentMarble.insert(marbleNumber)
        }
    }

    val max = playerScores.values.max()
    println("max score = $max")
}
