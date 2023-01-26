import kotlin.math.pow
import kotlin.math.sqrt

fun perimeter(
    x1: Double,
    y1: Double,
    x2: Double,
    y2: Double,
    x3: Double,
    y3: Double,
    x4: Double = x1,
    y4: Double = y1
): Double {
    val x1y1x2y2 = sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2))
    val x2y2x3y3 = sqrt((x3 - x2).pow(2) + (y3 - y2).pow(2))
    val x3y3x4y4 = sqrt((x4 - x3).pow(2) + (y4 - y3).pow(2))
    val x4y4x1y1 = sqrt((x1 - x4).pow(2) + (y1 - y4).pow(2))
    return x1y1x2y2 + x2y2x3y3 + x3y3x4y4 + x4y4x1y1
}