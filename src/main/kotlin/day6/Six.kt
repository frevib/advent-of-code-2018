import kotlin.math.absoluteValue

fun main() {
    val input = inputReal
    val inputListInt = input.split("\n").map { it ->
        Pair(it.split(", ")[0].toInt(), it.split(", ")[1].toInt())
    }

    val maxPair = inputListInt.maxBy { it.first or it.second }
    val max = if (maxPair!!.first > maxPair.second) maxPair.first else maxPair.second

    val axisLength = max
    val grid1 =  Array(axisLength+1) { IntArray(axisLength+1) }

    val edgeNumbers = HashSet<Int>()
    for (i in 0..axisLength) {
        for (j in 0..axisLength) {
            var minDistance = max * 10
            for ((idx, coordinates) in inputListInt.withIndex()) {
                if (i == 0 || i == max || j == 0 || j == max) {
                    edgeNumbers.add(idx)
                }

                val taxiDistance = (coordinates.first - i).absoluteValue + (coordinates.second - j).absoluteValue

                if (taxiDistance == minDistance) {
                    grid1[i][j] = -1
                }
                else if (taxiDistance < minDistance) {
                    minDistance = taxiDistance
                    grid1[i][j] = idx
                }
            }
        }
    }


//    println(edgeNumbers)

    val someSet = setOf(grid1[0].asList().union(grid1[max].asList()))

//    grid1[0].forEach { it -> println(it) }
    println(someSet.distinct())
//    someSet.forEach { it -> println(it) }

    println(grid1[221][359])
//    println(inputListInt)

}



const val inputReal = """183, 157
331, 86
347, 286
291, 273
285, 152
63, 100
47, 80
70, 88
333, 86
72, 238
158, 80
256, 140
93, 325
343, 44
89, 248
93, 261
292, 250
240, 243
342, 214
192, 51
71, 92
219, 63
240, 183
293, 55
316, 268
264, 151
68, 98
190, 288
85, 120
261, 59
84, 222
268, 171
205, 134
80, 161
337, 326
125, 176
228, 122
278, 151
129, 287
293, 271
57, 278
104, 171
330, 69
141, 141
112, 127
201, 151
331, 268
95, 68
289, 282
221, 359"""
