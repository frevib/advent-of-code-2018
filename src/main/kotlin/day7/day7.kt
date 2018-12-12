package day7

import kotlin.collections.HashMap

val inputReal = """Step R must be finished before step Y can begin.
Step N must be finished before step T can begin.
Step C must be finished before step L can begin.
Step F must be finished before step B can begin.
Step L must be finished before step D can begin.
Step T must be finished before step D can begin.
Step O must be finished before step E can begin.
Step M must be finished before step Z can begin.
Step A must be finished before step V can begin.
Step K must be finished before step D can begin.
Step W must be finished before step I can begin.
Step B must be finished before step J can begin.
Step H must be finished before step D can begin.
Step P must be finished before step J can begin.
Step J must be finished before step Z can begin.
Step S must be finished before step X can begin.
Step Z must be finished before step U can begin.
Step Y must be finished before step E can begin.
Step V must be finished before step I can begin.
Step U must be finished before step Q can begin.
Step Q must be finished before step D can begin.
Step X must be finished before step I can begin.
Step G must be finished before step E can begin.
Step I must be finished before step D can begin.
Step D must be finished before step E can begin.
Step B must be finished before step S can begin.
Step U must be finished before step E can begin.
Step J must be finished before step G can begin.
Step J must be finished before step G can begin.
Step I must be finished before step E can begin.
Step N must be finished before step G can begin.
Step P must be finished before step Z can begin.
Step X must be finished before step D can begin.
Step H must be finished before step V can begin.
Step Q must be finished before step E can begin.
Step Z must be finished before step D can begin.
Step V must be finished before step D can begin.
Step S must be finished before step Q can begin.
Step F must be finished before step O can begin.
Step F must be finished before step M can begin.
Step W must be finished before step B can begin.
Step J must be finished before step X can begin.
Step G must be finished before step D can begin.
Step R must be finished before step K can begin.
Step L must be finished before step Y can begin.
Step J must be finished before step Q can begin.
Step Z must be finished before step E can begin.
Step Y must be finished before step Q can begin.
Step K must be finished before step P can begin.
Step N must be finished before step B can begin.
Step Q must be finished before step I can begin.
Step P must be finished before step U can begin.
Step F must be finished before step J can begin.
Step L must be finished before step G can begin.
Step Q must be finished before step X can begin.
Step H must be finished before step G can begin.
Step C must be finished before step O can begin.
Step V must be finished before step G can begin.
Step M must be finished before step G can begin.
Step A must be finished before step Z can begin.
Step C must be finished before step A can begin.
Step N must be finished before step P can begin.
Step N must be finished before step L can begin.
Step W must be finished before step E can begin.
Step N must be finished before step U can begin.
Step A must be finished before step U can begin.
Step O must be finished before step G can begin.
Step Y must be finished before step X can begin.
Step P must be finished before step S can begin.
Step Z must be finished before step Q can begin.
Step K must be finished before step S can begin.
Step N must be finished before step Z can begin.
Step Z must be finished before step V can begin.
Step P must be finished before step Y can begin.
Step L must be finished before step I can begin.
Step O must be finished before step P can begin.
Step N must be finished before step A can begin.
Step F must be finished before step A can begin.
Step P must be finished before step E can begin.
Step Z must be finished before step X can begin.
Step O must be finished before step A can begin.
Step F must be finished before step K can begin.
Step T must be finished before step U can begin.
Step Z must be finished before step I can begin.
Step N must be finished before step O can begin.
Step K must be finished before step U can begin.
Step M must be finished before step W can begin.
Step O must be finished before step U can begin.
Step S must be finished before step I can begin.
Step N must be finished before step K can begin.
Step O must be finished before step J can begin.
Step C must be finished before step J can begin.
Step W must be finished before step S can begin.
Step W must be finished before step J can begin.
Step H must be finished before step J can begin.
Step G must be finished before step I can begin.
Step V must be finished before step U can begin.
Step O must be finished before step H can begin.
Step F must be finished before step Y can begin.
Step U must be finished before step D can begin.
Step N must be finished before step E can begin.
Step H must be finished before step P can begin."""

val inputTest = """Step C must be finished before step A can begin.
Step A must be finished before step B can begin.
Step A must be finished before step D can begin.
Step B must be finished before step E can begin.
Step D must be finished before step E can begin.
Step F must be finished before step E can begin.
Step C must be finished before step F can begin."""

class Node(var name: Char) {
    val parents: HashMap<Char, Node> = HashMap()
    val children: HashMap<Char, Node> = HashMap()
}


fun main(args : Array<String>) {
    star1(inputReal)
    star2(inputReal)
}


fun star1(input: String) {

    // create list of pairs of numbers
    val inputList = input.split("\n").map { it ->
        Pair(it.substring(5,6).single(), it.substring(36,37).single())
    }

    // create nodes with children and parents
    val nodeMap = HashMap<Char, Node>()
    val allLetters = inputList
            .map { it -> listOf(it.first, it.second) }
            .flatMap { it -> it.toList() }
            .distinct()
            .sorted()


    allLetters.forEach { char ->
        nodeMap[char] = Node(char)
        nodeMap[char]!!.name
    }

    inputList.forEach { pair ->
        nodeMap[pair.first]!!.children[pair.second] = nodeMap[pair.second]!!
        nodeMap[pair.second]!!.parents[pair.first] = nodeMap[pair.first]!!
    }

    // determine first nodes and add them to root nodes
    val rootNode = Node('x')
    nodeMap
            .filter { it -> it.value.parents.size == 0 }
            .forEach { it ->
                rootNode.children[it.key] = it.value
                rootNode.parents['x'] = rootNode
            }

    nodeMap['x'] = rootNode


    // find letter order
    val seenLetters = sortedSetOf<Char>()
    seenLetters.add('x')
    val candidates = sortedSetOf<Char>()
    candidates.add('x')
    var answerStar1 = ""

    while (seenLetters.size < allLetters.size +1) {
        for (candidate in candidates) {
            if (seenLetters.containsAll(nodeMap[candidate]?.parents!!.keys)) {
                answerStar1 += candidate

                seenLetters.add(candidate)
                candidates.remove(candidate)

                candidates.run {
                    nodeMap[candidate]?.children?.keys?.let { addAll(it) }

                }
                break
            }
        }
    }

    println("Answer star 1: ${answerStar1.substring(1)}")
}

fun star2(input: String) {

    // create list of pairs of numbers
    val inputList = input.split("\n").map { it ->
        Pair(it.substring(5,6).single(), it.substring(36,37).single())
    }

    // create nodes with children and parents
    val nodeMap = HashMap<Char, Node>()
    val allLetters = inputList
            .map { it -> listOf(it.first, it.second) }
            .flatMap { it -> it.toList() }
            .distinct()
            .sorted()


    allLetters.forEach { char ->
        nodeMap[char] = Node(char)
        nodeMap[char]!!.name
    }

    inputList.forEach { pair ->
        nodeMap[pair.first]!!.children[pair.second] = nodeMap[pair.second]!!
        nodeMap[pair.second]!!.parents[pair.first] = nodeMap[pair.first]!!
    }

    // determine first nodes and add them to root nodes
    val rootNode = Node('x')
    nodeMap
            .filter { it -> it.value.parents.size == 0 }
            .forEach { it ->
                rootNode.children[it.key] = it.value
                rootNode.parents['x'] = rootNode
            }

    nodeMap['x'] = rootNode


    // find seconds
    val seenLetters = sortedSetOf<Char>()
    seenLetters.add('x')
    var candidates = sortedSetOf<Char>()

    var busyLetters = mutableMapOf<Char, Int>()
    busyLetters['x'] = 0
    var freeElves = 4
    var secondsPassed = -1

    while (true) {
        val busyLettersCopy =  LinkedHashMap(busyLetters)

        // handle completed letters
        for (i in busyLetters) {
            if (i.value == secondsPassed) {
                busyLettersCopy.remove(i.key)
                seenLetters.add(i.key)
                freeElves++
                candidates.addAll(nodeMap[i.key]!!.children.keys)
            }
        }
        busyLetters = busyLettersCopy


        val candidatesCopy = sortedSetOf<Char>()
        candidatesCopy.addAll(candidates)

        val freeElvesCopy = freeElves

        for (i in 1..freeElvesCopy) {
            for (candidate in candidates) {

                if (seenLetters.containsAll(nodeMap[candidate]?.parents!!.keys) &&
                        !busyLetters.containsKey(candidate) &&
                        freeElves != 0) {
                    candidatesCopy.remove(candidate)
                    busyLetters[candidate] = (candidate - 'A' + secondsPassed + 61)
                    freeElves--
                }
            }
        }

        candidates = candidatesCopy

        if (seenLetters.size == allLetters.size + 1) {
            break
        }

        secondsPassed++
    }

    println("Answer star2: $secondsPassed")
}



























