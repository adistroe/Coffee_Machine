fun main() {
    val timerValue = readln().toInt()
    val timer = ByteTimer(timerValue)
    println(timer.time)
}

class ByteTimer(var time: Int) {
    init {
        if (time !in Byte.MIN_VALUE..Byte.MAX_VALUE) {
            time = if (time < 0) -128 else 127
        }
    }
}