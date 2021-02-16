package sorting

import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    val input = mutableListOf<Int>()

    while (scanner.hasNextInt()) {
        input.add(scanner.nextInt())
    }

    val x = input.count()
    val y = input.maxOrNull()
    val z = input.count { it == y }

    println("Total numbers: $x.")
    println("The greatest number: $y ($z time(s)).")
}
