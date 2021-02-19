package sorting

import sorting.DataType.*
import java.util.Scanner
import kotlin.math.roundToInt

enum class DataType(val id: Int) {
    LONG(1),
    LINE(2),
    WORD(4);
}

enum class ProcessType(val id: Int) {
    SUMMARY(8),
    NATURAL(16),
    BYCOUNT(32);
}

fun main(args: Array<String>) {

    val scanner = Scanner(System.`in`)
    val choices = parseInput(args)
    process(choices, scanner)

}

fun process(choices: Int, scanner: Scanner) {
    val longInput: MutableList<Long>?
    val stringInput: MutableList<String>?

    when {
        // what to read
        choices and LONG.id != 0 -> {
            longInput = readNumbers(scanner)
            when {
                // what to do
                choices and ProcessType.NATURAL.id != 0 -> sortNatural(longInput, "numbers", " ")
                choices and ProcessType.BYCOUNT.id != 0 -> sortByCount(longInput, "numbers")
                choices and ProcessType.SUMMARY.id != 0 -> summarizeNumbers(longInput)
            }
        }
        choices and LINE.id != 0 -> {
            stringInput = readLines(scanner)
            when {
                // what to do
                choices and ProcessType.NATURAL.id != 0 -> sortNatural(stringInput, "lines", "\n")
                choices and ProcessType.BYCOUNT.id != 0 -> sortByCount(stringInput, "lines")
                choices and ProcessType.SUMMARY.id != 0 -> summarizeLines(stringInput)
            }

        }
        choices and WORD.id != 0 -> {
            stringInput = readWords(scanner)
            when {
                // what to do
                choices and ProcessType.NATURAL.id != 0 -> sortNatural(stringInput, "words", " ")
                choices and ProcessType.BYCOUNT.id != 0 -> sortByCount(stringInput, "words")
                choices and ProcessType.SUMMARY.id != 0 -> summarizeWords(stringInput)
            }
        }
    }

}

fun parseInput(args: Array<String>): Int {
//    if (args.contains("-sortIntegers")) {
//        val (x, y, z, data) = readNumbers(scanner)
//        println("Total numbers: $x.")
//        println("Sorted data: ${data.sorted().joinToString(" ")}")
//        return
//    }
    var choice = 0
    choice += if (args.contains("-sortingType") &&
        args.indexOf("-sortingType") + 1 < args.size
    ) {
        val idx: Int = args.indexOf("-sortingType") + 1
        when (ProcessType.valueOf(args[idx].toUpperCase())) {
            ProcessType.NATURAL -> ProcessType.NATURAL.id
            ProcessType.BYCOUNT -> ProcessType.BYCOUNT.id
            ProcessType.SUMMARY -> ProcessType.SUMMARY.id
        }
    } else
        ProcessType.NATURAL.id

    choice += if (args.contains("-dataType") &&
        args.indexOf("-dataType") + 1 < args.size
    ) {
        val idx: Int = args.indexOf("-dataType") + 1
        when (valueOf(args[idx].toUpperCase())) {
            LONG -> LONG.id
            LINE -> LINE.id
            WORD -> WORD.id
        }
    } else
        LONG.id

    return choice
}

fun readNumbers(scanner: Scanner): MutableList<Long> {
    val input = mutableListOf<Long>()
    while (scanner.hasNextInt()) {
        input.add(scanner.nextLong())
    }
    return input
}

fun summarizeNumbers(input: MutableList<Long>) {
    val x = input.count()
    val y = input.maxOrNull()!!
    val z = input.count { it == y }

    println("Total numbers: $x.")
    println(
        "The greatest number: $y " +
                "($z time(s), ${(z / x.toDouble() * 100).toInt()}%)."
    )
}

fun readLines(scanner: Scanner): MutableList<String> {
    val input = mutableListOf<String>()
    while (scanner.hasNextLine()) {
        input.add(scanner.nextLine())
    }
    return input
}

fun summarizeLines(input: MutableList<String>) {
    val x = input.count()
    val y = input.maxByOrNull { it.length }
    val z = input.count { it == y }

    println("Total lines: $x.")
    println("The longest line: \n$y")
    println("($z time(s), ${(z / x.toDouble() * 100).toInt()}%).")
}

fun readWords(scanner: Scanner): MutableList<String> {
    val input = mutableListOf<String>()
    while (scanner.hasNext()) {
        input.add(scanner.next())
    }
    return input
}

fun summarizeWords(input: MutableList<String>) {
    val x = input.count()
    val y = input.maxByOrNull { it.length }
    val z = input.count { it == y }

    println("Total words: $x.")
    println(
        "The longest word: $y " +
                "($z time(s), ${(z / x.toDouble() * 100).toInt()}%)."
    )
}

fun <T : Comparable<T>> sortNatural(
    input: MutableList<T>,
    element: String,
    sep: String
) {
    println("Total $element: ${input.count()}.")
    println(input.sorted().joinToString(sep))
}

fun <T : Comparable<T>> sortByCount(
    input: MutableList<T>,
    element: String
) {
    val count = input.count()
    println("Total $element: $count.")
    val byCount = input.groupingBy { it }.eachCount()
    val sorted = byCount.entries.sortedWith(compareBy({ it.value }, { it.key }))
    sorted.forEach { println("${it.key}: ${it.value} time(s), ${(it.value / count.toDouble() * 100.0).roundToInt()}%") }
}
