package sorting

import sorting.DataType.*
import java.util.Scanner
import kotlin.math.roundToInt

enum class DataType(val id: Int) {
    LONG(1),
    LINE(2),
    WORD(4),
    NULLDATA(0);
}

enum class ProcessType(val id: Int) {
    SUMMARY(8),
    NATURAL(16),
    BYCOUNT(32),
    NULLPROCESS(0);
}

class InValidArgumentOptionException(message: String): Exception(message)

fun main(args: Array<String>) {

    val scanner = Scanner(System.`in`)
    try {
        val validArgs = collect(args)
        val choices = parseInput(validArgs)
        process(choices, scanner)
    } catch (e: InValidArgumentOptionException) {
        println(e.message)
    }
}

fun collect(args: Array<String>): MutableMap<String, String> {
    val valid = mutableMapOf<String, String>()
    val validDataOptions = arrayOf("long", "line", "word")
    val validSortingOptions = arrayOf("natural", "byCount")
    for (arg in args) {
        when (arg) {
            "-dataType" -> {
                val idx = args.indexOf("-dataType") + 1
                valid[arg] = if (idx < args.size && args[idx] in validDataOptions)
                    args[idx].toUpperCase()
                else throw InValidArgumentOptionException("No data type defined!")
            }
            "-sortingType" -> {
                val idx = args.indexOf("-sortingType") + 1
                valid[arg] = if (idx < args.size && args[idx] in validSortingOptions)
                    args[idx].toUpperCase()
                else throw InValidArgumentOptionException("No sorting type defined!")
            }
            in validDataOptions -> {
            }
            in validSortingOptions -> {
            }
            else -> println("$arg is not a valid parameter. It will be skipped.")
        }
    }
    if (valid.size == 1) {
        if (!valid.containsKey("-dataType")) valid["-dataType"] = "LONG"
        else if (!valid.containsKey("-sortingType")) valid["-sortingType"] = "NATURAL"
    }
    return valid
}

fun parseInput(args: MutableMap<String, String>): Int {

    var choice = 0
    val dataType = DataType.valueOf(args["-dataType"].toString())
    choice += when (dataType) {
        LONG -> LONG.id
        LINE -> LINE.id
        WORD -> WORD.id
        else -> NULLDATA.id
    }

    choice += when (ProcessType.valueOf(args["-sortingType"].toString())) {
        ProcessType.NATURAL -> ProcessType.NATURAL.id
        ProcessType.BYCOUNT -> ProcessType.BYCOUNT.id
        ProcessType.SUMMARY -> ProcessType.SUMMARY.id
        else -> ProcessType.NULLPROCESS.id
    }

    return choice
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

fun readNumbers(scanner: Scanner): MutableList<Long> {
    val input = mutableListOf<Long>()
//    while (scanner.hasNextInt()) {
//        input.add(scanner.nextLong())
//    }
    while (scanner.hasNextLine()) {
        val line = scanner.nextLine()
        val temp = line.split(Regex("""\s+""")).toTypedArray()
        for (token in temp) {
            try {
                input.add(token.toLong())
            } catch (e: Exception) {
                println(
                    """
                    "$token" is not a long. It will be skipped.
                """.trimIndent()
                )
            }
        }
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
    print("Sorted data: ")
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
