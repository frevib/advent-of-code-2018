package day16

import java.io.File
import kotlin.test.assertTrue

fun main() {
    val input = readFile("src/main/resources/day16.txt")
    val parts = input.split("\n\n\n\n")
    val part1 = parts[0]
    val part2 = parts[1]

    star2(part1, part2)
}

fun readFile(filePath: String): String {
    return File(filePath).readText()
}

class Block(block: String) {

    var currentRegisters: Array<Int>
    var instructions: Array<Int>
    var result: Array<Int>

    init {
        val blockSplit = block.split("\n")

        this.currentRegisters = blockSplit[0]
                .substring(9,19)
                .split(", ")
                .map { it -> it.toInt() }
                .toTypedArray()

        this.instructions = blockSplit[1]
                .split(" ")
                .map { it -> it.toInt() }
                .toTypedArray()

        this.result = blockSplit[2]
                .substring(9,19)
                .split(", ")
                .map { it -> it.toInt() }
                .toTypedArray()
    }
}

fun star2(input: String, part2: String) {

    val blocks = input
            .split("\n\n")
            .map { it ->  Block(it)}

    val instructionsMapping = mutableMapOf<String, (Array<Int>, Array<Int>) ->  Array<Int>>()

    // add
    instructionsMapping["addr"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        newCurrentRegisters[instructions[3]] = newCurrentRegisters[instructions[1]] + newCurrentRegisters[instructions[2]]
        newCurrentRegisters
     }

    instructionsMapping["addi"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        newCurrentRegisters[instructions[3]] = newCurrentRegisters[instructions[1]] + instructions[2]
        newCurrentRegisters
    }


    // 3
    // multiplication
    instructionsMapping["mulr"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        newCurrentRegisters[instructions[3]] = newCurrentRegisters[instructions[1]] * newCurrentRegisters[instructions[2]]
        newCurrentRegisters
     }

    instructionsMapping["muli"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        newCurrentRegisters[instructions[3]] = newCurrentRegisters[instructions[1]] * instructions[2]
        newCurrentRegisters
    }

    // 5
    // bitwise AND
    instructionsMapping["banr"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        newCurrentRegisters[instructions[3]] = newCurrentRegisters[instructions[1]] and newCurrentRegisters[instructions[2]]
        newCurrentRegisters
    }

    instructionsMapping["bani"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        newCurrentRegisters[instructions[3]] = newCurrentRegisters[instructions[1]] and instructions[2]
        newCurrentRegisters
    }

    // 7
    // bitwise OR
    instructionsMapping["borr"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        newCurrentRegisters[instructions[3]] = newCurrentRegisters[instructions[1]] or newCurrentRegisters[instructions[2]]
        newCurrentRegisters
    }

    instructionsMapping["bori"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        newCurrentRegisters[instructions[3]] = newCurrentRegisters[instructions[1]] or instructions[2]
        newCurrentRegisters
    }

    // 9
    // assignment
    instructionsMapping["setr"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        newCurrentRegisters[instructions[3]] = newCurrentRegisters[instructions[1]]
        newCurrentRegisters
    }

    instructionsMapping["seti"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        newCurrentRegisters[instructions[3]] = instructions[1]
        newCurrentRegisters
    }

    // 11
    // greater than
    instructionsMapping["gtir"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        if (instructions[1] > newCurrentRegisters[instructions[2]]) {
            newCurrentRegisters[instructions[3]] = 1
        } else {
            newCurrentRegisters[instructions[3]] = 0
        }
        newCurrentRegisters
    }

    instructionsMapping["gtri"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        if (newCurrentRegisters[instructions[1]] > instructions[2]) {
            newCurrentRegisters[instructions[3]] = 1
        } else {
            newCurrentRegisters[instructions[3]] = 0
        }
        newCurrentRegisters
    }

    instructionsMapping["gtrr"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        if (newCurrentRegisters[instructions[1]] > newCurrentRegisters[instructions[2]]) {
            newCurrentRegisters[instructions[3]] = 1
        } else {
            newCurrentRegisters[instructions[3]] = 0
        }
        newCurrentRegisters
    }

    // 14
    // equality
    instructionsMapping["eqir"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()

        if (instructions[1] == newCurrentRegisters[instructions[2]]) {
            newCurrentRegisters[instructions[3]] = 1
        } else {
            newCurrentRegisters[instructions[3]] = 0
        }
        newCurrentRegisters
    }

    instructionsMapping["eqri"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        if (newCurrentRegisters[instructions[1]] == instructions[2]) {
            newCurrentRegisters[instructions[3]] = 1
        } else {
            newCurrentRegisters[instructions[3]] = 0
        }
        newCurrentRegisters
    }

    instructionsMapping["eqrr"] = { currentRegisters: Array<Int>, instructions: Array<Int> ->
        val newCurrentRegisters = currentRegisters.copyOf()
        if (newCurrentRegisters[instructions[1]] == newCurrentRegisters[instructions[2]]) {
            newCurrentRegisters[instructions[3]] = 1
        } else {
            newCurrentRegisters[instructions[3]] = 0
        }
        newCurrentRegisters
    }

    // deduce opcodes by checking all possible answers that occur only once
    val opcodes = mutableMapOf<Int, String>()
    blocks.forEach loop1@ { block ->
        if (block.instructions[0] in opcodes.keys) {  // skip known opcodes
            return@loop1
        }

        val answers = mutableMapOf<String, String>()
        instructionsMapping.forEach loop2@ { instruction ->
            if (instruction.key in opcodes.values) { // skip executing known instructions
                return@loop2
            }

            val answer = instruction.value.invoke(block.currentRegisters, block.instructions).joinToString("") // execute instruction
            answers[instruction.key] = answer
        }

        val count = answers.count { it -> it.value == block.result.joinToString("") }
        if (count == 1) { // if there is only one matching answer, add the opcode to known opcodes
            val instructionName = answers
                    .filter { it -> it.value == block.result.joinToString("") }
                    .keys
                    .first()

            val opcode = block.instructions[0]
            opcodes[opcode] = instructionName // put now known opcode in opcode map
        }
    }


    // parse input of second star
    var part2Lines = part2.split("\n")
    part2Lines = part2Lines.subList(0, part2Lines.lastIndex)

    // execute instructions of part2
    var registers = Array(4) {0}
    part2Lines.forEach { instruction ->
        val instructionArray = instruction
                .split(" ")
                .map { it -> it.toInt() }
                .toTypedArray()

        val opcode = instructionArray[0]
        registers = instructionsMapping[opcodes[opcode]]?.invoke(registers, instructionArray)!!
    }

    println("Answer star2: ${registers[0]}")
//    validatePart1(blocks, instructionsMapping, opcodes)

}


fun validatePart1(
        blocks: List<Block>,
        instructionMapping: Map<String, (Array<Int>, Array<Int>) -> Array<Int>>,
        opcodes: Map<Int, String>) {

    blocks.forEach { block ->
        val result = instructionMapping[opcodes[block.instructions[0]]]?.invoke(block.currentRegisters, block.instructions)
        assertTrue(result!! contentDeepEquals block.result)
    }
}
