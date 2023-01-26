fun main() {
    val firstList = readln().split(' ').map { it }.toMutableList()
    val secondList = readln().split(' ').map { it }.toMutableList()
    // do not touch the lines above
    // write your code here
    println((firstList + secondList).joinToString())
}