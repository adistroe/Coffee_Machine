fun f(x: Double): Double {
    // call your implemented functions here
    return when (x) {
        in -Double.MAX_VALUE..0.0 -> f1(x)
        in 1.0..Double.MAX_VALUE -> f3(x)
        else -> f2(x)   // 0 < x < 1
    }
}

// implement your functions here
fun f1(x: Double): Double = x * x + 1

fun f2(x: Double): Double = 1 / (x * x)

fun f3(x: Double): Double = x * x - 1

fun printLine(line: String = "", end: String = "\n") = print("$line$end")