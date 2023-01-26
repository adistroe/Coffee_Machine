package machine

const val EXIT = "exit"
const val WHAT_TO_DO = "\nWrite action (buy, fill, take, remaining, exit):"

fun main() {
    val coffeeMachine = CoffeeMachine()
    println(WHAT_TO_DO)
    do {
        val userInput = readln().lowercase()
        coffeeMachine.getUserInput(userInput)
    } while (userInput != EXIT)
}