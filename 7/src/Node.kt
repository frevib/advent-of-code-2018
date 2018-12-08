import kotlin.collections.HashMap
import kotlin.collections.HashSet

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
    var visited: Boolean = false
    val children: HashMap<Char, Node> = HashMap()
}


fun main(args : Array<String>) {
    val input = inputTest
    val inputList = input.split("\n").map { it ->
        Pair(it.substring(5,6).single(), it.substring(36,37).single())
    }

//    create root node with children
//    val childSet = inputList.map { it -> it.second }.toSet()

//    for (letter in 'A'..'F') {
//        if (!childSet.contains(letter)) {
////            println("root node: $letter")
////            rootNode.children.add(Node(letter))
//        }
//    }


    // create nodes with children and parents
    val nodeMap = HashMap<Char, Node>()

    for (char in 'A'..'F') {
        nodeMap[char] = Node(char)
    }

    for (pair in inputList) {
        nodeMap[pair.first]!!.children[pair.second] = nodeMap[pair.second]!!
        nodeMap[pair.second]!!.parents[pair.first] = nodeMap[pair.first]!!
    }

//    nodeMap['T']!!.children.forEach { println(it) }
//    nodeMap['A']!!.children.forEach { println(it) }
//    nodeMap['F']!!.children.forEach { println(it) }
//    nodeMap['E']!!.children.forEach { println(it) }


    val rootNode = Node('x')
//    rootNode.children['C'] = nodeMap['C']!!
//    rootNode.children['F'] = nodeMap['F']!!
//    rootNode.children['N'] = nodeMap['N']!!
//    rootNode.children['R'] = nodeMap['R']!!

    rootNode.children['C'] = nodeMap['C']!!


    val currentLetters = rootNode.children.keys
    val seenLetters = HashSet<Char>()

    var freeElves = 5

    var currentSmallest = 'q'
    val busyLetters = mutableMapOf<Char, Int>()
    var secondsPassed = 0


    while (true) {
        // set busy letters

        // handle when step is completed
        busyLetters
            .filter { it -> it.value == secondsPassed }.keys.sorted()
            .also { freeElves += it.size }
            .also { seenLetters.addAll(it) }
            .forEach { busyLetters.remove(it) }


        // add busy letters

        for (i in 0..freeElves) {
            nodeMap[]
        }



        if (seenLetters.size > 25) {
            break
        }

        secondsPassed++
    }

    println("amount of seconds taken: $secondsPassed")







//    while (seenValues.size < 6) {
//
//        currentSmallest = childNodesChar.min()!!
//
//        while (freeElves != 0) {
//            if (childNodesChar.min() != null) {
//
//                val letter = childNodesChar.min()
//                childNodesChar.remove(letter)
//                currentLetters.add(letter!!)
//                freeElves--
//
//            } else {
//                println("break")
//                break
//            }
//        }
//
//        seenValues.addAll(currentLetters)
////        if (seenValues.containsAll(nodeMap[letter]!!.parents.keys)) {
////            print(letter)
////            seenValues.add(letter!!)
////        }
//
////        childNodesChar.remove(letter)
//
////        childNodesChar = childNodesChar.union(nodeMap[letter]!!.children.keys) as MutableSet<Char>
////        println(values)
//    }

}



























