package sorting

import java.util.Scanner

data class LongInput(val total: Int, val greatest: Int, val frequency: Int, val data: MutableList<Int>)
data class LineInput(val total: Int, val longestLine: String, val frequency: Int)
data class WordInput(val total: Int, val longestWord: String, val frequency: Int)

fun main(args: Array<String>) {

    val scanner = Scanner(System.`in`)
    parseInput(args, scanner)

}

fun parseInput(args: Array<String>, scanner: Scanner) {
    if (args.contains("-sortIntegers")) {
        val (x, y, z, data) = readNumbers(scanner)
        println("Total numbers: $x.")
        println("Sorted data: ${data.sorted().joinToString(" ")}")
        return
    }

    if (args[0] == "-dataType") {
        when (args[1]) {
            "long" -> {
                val (x, y, z, data) = readNumbers(scanner)
                println("Total numbers: $x.")
                println(
                    "The greatest number: $y " +
                            "($z time(s), ${(z / x.toDouble() * 100).toInt()}%)."
                )
            }
            "line" -> {
                val (x, y, z) = readLines(scanner)
                println("Total lines: $x.")
                println("The longest line: \n$y")
                println("($z time(s), ${(z / x.toDouble() * 100).toInt()}%).")
            }
            "word" -> {
                val result = readWords(scanner)
                val (x, y, z) = result
                println("Total words: $x.")
                println("The longest word: $y " +
                        "($z time(s), ${(z / x.toDouble() * 100).toInt()}%)."
                )
            }
        }
    } else {
        val result = readWords(scanner)
        val (x, y, z) = result
        println("Total words: $x.")
        println("The longest word: $y " +
                "($z time(s), ${(z / x.toDouble() * 100).toInt()}%)."
        )
    }
}

fun readNumbers(scanner: Scanner): LongInput {
    val input = mutableListOf<Int>()
    while (scanner.hasNextInt()) {
        input.add(scanner.nextInt())
    }
    val x = input.count()
    val y = input.maxOrNull()!!
    val z = input.count { it == y }

    return LongInput(x, y, z, input)
}

fun readLines(scanner: Scanner): LineInput {
    val input = mutableListOf<String>()
    while (scanner.hasNextLine()) {
        input.add(scanner.nextLine())
    }
    val x = input.count()
    val y = input.maxByOrNull { it.length }
    val z = input.count { it == y }

    return LineInput(x, y as String, z)
}

fun readWords(scanner: Scanner): WordInput {
    val input = mutableListOf<String>()
    while (scanner.hasNext()) {
        input.add(scanner.next())
    }
    val x = input.count()
    val y = input.maxByOrNull { it.length }
    val z = input.count { it == y }

    return WordInput(x, y as String, z)
}
