package day12


fun main() {
    val inputTest = object {
        val initialState = "#..#.#..##......###...###"
        val notes = "...## => #\n" +
                "..#.. => #\n" +
                ".#... => #\n" +
                ".#.#. => #\n" +
                ".#.## => #\n" +
                ".##.. => #\n" +
                ".#### => #\n" +
                "#.#.# => #\n" +
                "#.### => #\n" +
                "##.#. => #\n" +
                "##.## => #\n" +
                "###.. => #\n" +
                "###.# => #\n" +
                "####. => #"
    }

    val inputReal = object {
        val initialState = "##....#.#.#...#.#..#.#####.#.#.##.#.#.#######...#.##....#..##....#.#..##.####.#..........#..#...#"
        val notes = "..#.# => #\n" +
                ".#### => #\n" +
                "#.... => .\n" +
                "####. => #\n" +
                "...## => .\n" +
                ".#.#. => .\n" +
                "..#.. => .\n" +
                "##.#. => .\n" +
                "#.#.# => #\n" +
                "..... => .\n" +
                "#.#.. => .\n" +
                "....# => .\n" +
                ".#..# => .\n" +
                "###.# => #\n" +
                "#..#. => .\n" +
                "##### => .\n" +
                "...#. => #\n" +
                "#.##. => #\n" +
                ".#.## => #\n" +
                "#..## => #\n" +
                ".##.. => #\n" +
                "##.## => .\n" +
                "..### => .\n" +
                "###.. => .\n" +
                "##..# => #\n" +
                ".#... => #\n" +
                ".###. => #\n" +
                "#.### => .\n" +
                ".##.# => .\n" +
                "#...# => #\n" +
                "##... => .\n" +
                "..##. => ."

    }

    star2(inputReal.initialState, inputReal.notes)
}



fun star2(initialState: String, notes: String) {
    val extensionSize = 1000

    var currentGenerationExtended =  "${".".repeat(10)}$initialState${".".repeat(extensionSize)}"
    val notesArray = notes
            .split("\n")
            .map { it -> it.split(" => ")}

    for (generation in 0 until 185) {
        val generationArray = Array(currentGenerationExtended.length) {'.'}

        for (pot in 0..currentGenerationExtended.length - 5) {
            val replace = notesArray.filter { it -> it[0] == currentGenerationExtended.substring(pot, pot + 5) }

            if (replace.isNotEmpty() && replace[0].size > 1) {
                generationArray[pot+2] = replace[0][1].single()
            }
        }

        currentGenerationExtended = generationArray.joinToString("")
//        println("first # at ${currentGenerationExtended.indexOf('#')} $generation ${generationArray.joinToString("")}")
    }

    println(currentGenerationExtended.trim('.'))
    println(currentGenerationExtended.trim('.').length)

    val begin = 141 + (50000000000L - 184L)
    var sum: Long = 0

    "##..#......##..#......##..#......##..#......##..#....##..#.......##..#......##..#....##..#.....##..#......##..#......##..#....##..#....##..#".forEachIndexed { index, c ->
        if (c == '#') {
            sum += (begin + index)
        }
    }


    println("answer star 2: $sum")

}
