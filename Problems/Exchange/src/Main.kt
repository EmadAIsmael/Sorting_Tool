fun main() {
    // put your code here
    val str = readLine()!!
    println(str.last() + str.substring(1, str.length - 1) + str.first())
}