package sorting

import sorting.DataType.*
import sorting.ProcessType.*
import java.io.File
import java.util.Scanner
import kotlin.math.roundToInt
import java.io.PrintStream


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

class InValidArgumentOptionException(message: String) : Exception(message)

class SortingTool {
    // Store current System.out before assigning a new value
    private val console = System.out
    private var scanner: Scanner = Scanner("")
    private var inFile = ""
    private lateinit var outFile: PrintStream
    private var printingToFile = false

    fun collect(args: Array<String>): MutableMap<String, String> {
        val valid = mutableMapOf<String, String>()
        val validDataOptions = arrayOf("long", "line", "word")
        val validSortingOptions = arrayOf("natural", "byCount")
        var inFileIdx = 0
        var outFileIdx = 0

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
                "-inputFile" -> {
                    inFileIdx = args.indexOf("-inputFile") + 1
                    if (inFileIdx < args.size) {
                        inFile = args[inFileIdx]
                        scanner = Scanner(inFile)
                    } else
                        scanner = Scanner(System.`in`)
                }
                "-outputFile" -> {
                    outFileIdx = args.indexOf("-outputFile") + 1
                    if (outFileIdx < args.size) {
                        val f = args[outFileIdx]
                        setPrintFile(f)
                        printingToFile = true
                    }
                }
                else -> {
                    when {
                        args.indexOf(arg) == inFileIdx -> {
                        }
                        args.indexOf(arg) == outFileIdx -> {
                        }
                        else -> println("$arg is not a valid parameter. It will be skipped.")
                    }
                }
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
            NATURAL -> NATURAL.id
            BYCOUNT -> BYCOUNT.id
            SUMMARY -> SUMMARY.id
            else -> NULLPROCESS.id
        }

        return choice
    }

    fun process(choices: Int) {
        val longInput: MutableList<Long>?
        val stringInput: MutableList<String>?

        when {
            // what to read
            choices and LONG.id != 0 -> {
                longInput = readNumbers()
                when {
                    // what to do
                    choices and NATURAL.id != 0 -> sortNatural(longInput, "numbers", " ")
                    choices and BYCOUNT.id != 0 -> sortByCount(longInput, "numbers")
                    choices and SUMMARY.id != 0 -> summarizeNumbers(longInput)
                }
            }
            choices and LINE.id != 0 -> {
                stringInput = readLines()
                when {
                    // what to do
                    choices and NATURAL.id != 0 -> sortNatural(stringInput, "lines", "\n")
                    choices and BYCOUNT.id != 0 -> sortByCount(stringInput, "lines")
                    choices and SUMMARY.id != 0 -> summarizeLines(stringInput)
                }

            }
            choices and WORD.id != 0 -> {
                stringInput = readWords()
                when {
                    // what to do
                    choices and NATURAL.id != 0 -> sortNatural(stringInput, "words", " ")
                    choices and BYCOUNT.id != 0 -> sortByCount(stringInput, "words")
                    choices and SUMMARY.id != 0 -> summarizeWords(stringInput)
                }
            }
        }
    }

    private fun readNumbers(): MutableList<Long> {
        val input = mutableListOf<Long>()
        var line: String? = " "
        while (line != null) {
            line = readLine()
            if (line == null) break
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

    private fun summarizeNumbers(input: MutableList<Long>) {
        val x = input.count()
        val y = input.maxOrNull()!!
        val z = input.count { it == y }

        println("Total numbers: $x.")
        println(
            "The greatest number: $y " +
                    "($z time(s), ${(z / x.toDouble() * 100).toInt()}%)."
        )
    }

    private fun readLines(): MutableList<String> {
        val input = mutableListOf<String>()
        var line: String? = " "
        while (line != null) {
            line = readLine()
            if (line == null) break
            input.add(line)
        }
        return input
    }

    private fun summarizeLines(input: MutableList<String>) {
        val x = input.count()
        val y = input.maxByOrNull { it.length }
        val z = input.count { it == y }

        println("Total lines: $x.")
        println("The longest line: \n$y")
        println("($z time(s), ${(z / x.toDouble() * 100).toInt()}%).")
    }

    private fun readWords(): MutableList<String> {
        val input = mutableListOf<String>()
        var line: String? = " "
        while (line != null) {
            line = readLine()
            if (line == null) break
            input.addAll(line.split(Regex("""\s+""")).toTypedArray())
        }
        return input
    }

    private fun summarizeWords(input: MutableList<String>) {
        val x = input.count()
        val y = input.maxByOrNull { it.length }
        val z = input.count { it == y }

        println("Total words: $x.")
        println(
            "The longest word: $y " +
                    "($z time(s), ${(z / x.toDouble() * 100).toInt()}%)."
        )
    }

    private fun <T : Comparable<T>> sortNatural(
        input: MutableList<T>,
        element: String,
        sep: String
    ) {
        if (printingToFile) printToFile()
        println("Total $element: ${input.count()}.")
        print("Sorted data: ")
        println(input.sorted().joinToString(sep))
        if (printingToFile) printToConsole()
    }

    private fun <T : Comparable<T>> sortByCount(
        input: MutableList<T>,
        element: String
    ) {
        if (printingToFile) printToFile()
        val count = input.count()
        println("Total $element: $count.")
        val byCount = input.groupingBy { it }.eachCount()
        val sorted = byCount.entries.sortedWith(compareBy({ it.value }, { it.key }))
        sorted.forEach { println("${it.key}: ${it.value} time(s), ${(it.value / count.toDouble() * 100.0).roundToInt()}%") }
        if (printingToFile) printToConsole()
    }

    private fun setPrintFile(toFile: String) {
        // Creating a File object that represents the disk file.
        outFile = PrintStream(File(toFile))
    }

    private fun printToFile() {
        // Assign outFile to output stream
        System.setOut(outFile)
        // println("This will be written to the text file")
    }

    private fun printToConsole() {
        // Use stored value for output stream
        System.setOut(this.console)
        // println("This will be written on the console!")
    }
}

fun main(args: Array<String>) {

    val sortingTool = SortingTool()
    try {
        val validArgs = sortingTool.collect(args)
        val choices = sortingTool.parseInput(validArgs)
        sortingTool.process(choices/*, scanner*/)
    } catch (e: InValidArgumentOptionException) {
        println(e.message)
    }
}
