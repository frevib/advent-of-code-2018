package day19

fun main() {
    val inputTest = """#ip 0
seti 5 0 1
seti 6 0 2
addi 0 1 0
addr 1 2 3
setr 1 0 0
seti 8 0 4
seti 9 0 5"""

    val input = """#ip 3
addi 3 16 3
seti 1 8 5
seti 1 0 4
mulr 5 4 2
eqrr 2 1 2
addr 2 3 3
addi 3 1 3
addr 5 0 0
addi 4 1 4
gtrr 4 1 2
addr 3 2 3
seti 2 3 3
addi 5 1 5
gtrr 5 1 2
addr 2 3 3
seti 1 4 3
mulr 3 3 3
addi 1 2 1
mulr 1 1 1
mulr 3 1 1
muli 1 11 1
addi 2 4 2
mulr 2 3 2
addi 2 19 2
addr 1 2 1
addr 3 0 3
seti 0 7 3
setr 3 2 2
mulr 2 3 2
addr 3 2 2
mulr 3 2 2
muli 2 14 2
mulr 2 3 2
addr 1 2 1
seti 0 1 0
seti 0 5 3"""

    star1(input.split("\n").toTypedArray())
    star2()
}

fun star1(inputLines: Array<String>) {

    val instructions = inputLines.drop(1)
    val ipBound = inputLines[0].substring(4).toInt()
    var ip = 0
    var registers = Array(6) { 0 }

    // for star 2
    // registers[0] = 1

    val instructionMapping = instructionMapping()

    while (ip < instructions.size) {
        val instructionLine = instructions[ip]
        val instructionName = instructionLine.take(4)
        val instructionRegisters = instructionLine
                .substring(5)
                .trim()
                .split(" ")
                .map { number -> number.toInt() }
                .toTypedArray()

        // set register 0 to ip
        registers[ipBound] = ip

        // execute instruction
        registers = instructionMapping[instructionName]!!.invoke(registers, instructionRegisters)
        ip = registers[ipBound]

        // increase ip with 1
        ip += 1
    }

    println("Answer star1: ${registers[0]}")
}


fun star2() {
    // reversing instructions
    // double loop, solution is to factorize loop number and add all factors together
    // number is: 10551343, factors are 11, 743, 1291
    // solution is 1+11+743+1291+8173+14201+959213+10551343 = 11534976
    println("Answer star2: 11534976")
}

fun instructionMapping(): Map<String, (Array<Int>, Array<Int>) -> Array<Int>> {
    val instructionsMapping = mutableMapOf<String, (Array<Int>, Array<Int>) ->  Array<Int>>()

    // add
    instructionsMapping["addr"] = { registers: Array<Int>, instructions: Array<Int> ->
        registers[instructions[2]] = registers[instructions[0]] + registers[instructions[1]]
        registers
    }

    instructionsMapping["addi"] = { registers: Array<Int>, instructions: Array<Int> ->
        registers[instructions[2]] = registers[instructions[0]] + instructions[1]
        registers
    }

    // multiplication
    instructionsMapping["mulr"] = { registers: Array<Int>, instructions: Array<Int> ->
        registers[instructions[2]] = registers[instructions[0]] * registers[instructions[1]]
        registers
    }

    instructionsMapping["muli"] = { registers: Array<Int>, instructions: Array<Int> ->
        registers[instructions[2]] = registers[instructions[0]] * instructions[1]
        registers
    }

    // bitwise AND
    instructionsMapping["banr"] = { registers: Array<Int>, instructions: Array<Int> ->
        registers[instructions[2]] = registers[instructions[0]] and registers[instructions[1]]
        registers
    }

    instructionsMapping["bani"] = { registers: Array<Int>, instructions: Array<Int> ->
        registers[instructions[2]] = registers[instructions[0]] and instructions[1]
        registers
    }

    // bitwise OR
    instructionsMapping["borr"] = { registers: Array<Int>, instructions: Array<Int> ->
        registers[instructions[2]] = registers[instructions[0]] or registers[instructions[1]]
        registers
    }

    instructionsMapping["bori"] = { registers: Array<Int>, instructions: Array<Int> ->
        registers[instructions[2]] = registers[instructions[0]] or instructions[1]
        registers
    }

    // assignment
    instructionsMapping["setr"] = { registers: Array<Int>, instructions: Array<Int> ->
        registers[instructions[2]] = registers[instructions[0]]
        registers
    }

    instructionsMapping["seti"] = { registers: Array<Int>, instructions: Array<Int> ->
        registers[instructions[2]] = instructions[0]
        registers
    }

    // greater than
    instructionsMapping["gtir"] = { registers: Array<Int>, instructions: Array<Int> ->
        if (instructions[0] > registers[instructions[1]]) registers[instructions[2]] = 1 else registers[instructions[2]] = 0
        registers
    }

    instructionsMapping["gtri"] = { registers: Array<Int>, instructions: Array<Int> ->
        if (registers[instructions[0]] > instructions[1]) registers[instructions[2]] = 1 else registers[instructions[2]] = 0
        registers
    }

    instructionsMapping["gtrr"] = { registers: Array<Int>, instructions: Array<Int> ->
        if (registers[instructions[0]] > registers[instructions[1]]) registers[instructions[2]] = 1 else registers[instructions[2]] = 0
        registers
    }

    // equality
    instructionsMapping["eqir"] = { registers: Array<Int>, instructions: Array<Int> ->
        if (instructions[1] == registers[instructions[1]]) registers[instructions[2]] = 1 else registers[instructions[2]] = 0
        registers
    }

    instructionsMapping["eqri"] = { registers: Array<Int>, instructions: Array<Int> ->
        if (registers[instructions[0]] == instructions[1]) registers[instructions[2]] = 1 else registers[instructions[2]] = 0
        registers
    }

    instructionsMapping["eqrr"] = { registers: Array<Int>, instructions: Array<Int> ->
        if (registers[instructions[0]] == registers[instructions[1]]) registers[instructions[2]] = 1 else registers[instructions[2]] = 0
        registers
    }

    return instructionsMapping.toMap()
}
