package machine

class CoffeeMachine(
    private var availableWater: Int = Inventory.WATER.startAmount,
    private var availableMilk: Int = Inventory.MILK.startAmount,
    private var availableCoffee: Int = Inventory.COFFEE.startAmount,
    private var availableCups: Int = Inventory.CUPS.startAmount,
    private var availableMoney: Int = Inventory.MONEY.startAmount,
) {
    //  default state on startup
    private var machineState = Machine.WAITING

    private enum class Inventory(val startAmount: Int) {
        WATER(400),
        MILK(540),
        COFFEE(120),
        CUPS(9),
        MONEY(550)
    }

    //  recipes for each coffee type
    enum class Recipe(val id: String, val water: Int, val milk: Int, val coffee: Int, val cost: Int) {
        ESPRESSO("1", 250, 0, 16, 4),
        LATTE("2", 350, 75, 20, 7),
        CAPPUCCINO("3", 200, 100, 12, 6);

        //  returns coffee recipe based on its id
        companion object {
            fun getById(id: String): Recipe {
                return when (id) {
                    ESPRESSO.id -> ESPRESSO
                    LATTE.id -> LATTE
                    else -> CAPPUCCINO
                }
            }
        }
    }

    private enum class Prompt(val text: String) {
        WHAT_TO_DO("\nWrite action (buy, fill, take, remaining, exit):"),
        WHAT_TO_BUY("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:"),
        HOW_MUCH_WATER("\nWrite how many ml of water you want to add:"),
        HOW_MUCH_MILK("Write how many ml of milk you want to add:"),
        HOW_MUCH_COFFEE("Write how many grams of coffee beans you want to add:"),
        HOW_MANY_CUPS("Write how many disposable cups you want to add:")
    }

    private enum class Message(val text: String) {
        MAKING_COFFEE("I have enough resources, making you a coffee!\n"),
        NOT_ENOUGH_WATER("Sorry, not enough water!\n"),
        NOT_ENOUGH_MILK("Sorry, not enough milk!\n"),
        NOT_ENOUGH_COFFEE("Sorry, not enough coffee!\n"),
        OUT_OF_CUPS("Sorry, not enough disposable cups!\n"),
        MACHINE_HAS("\nThe coffee machine has:"),
        WATER("\n%d ml of water"),
        MILK("\n%d ml of milk"),
        COFFEE("\n%d g of coffee beans"),
        CUPS("\n%d disposable cups"),
        CURRENCY_SYMBOL("$"),
        MONEY("\n${CURRENCY_SYMBOL.text}%d of money"),
        GAVE_MONEY("\nI gave you ${CURRENCY_SYMBOL.text}%d")
    }

    //  switching machine between these states allows it to make sense of the user input
    private enum class Machine(val state: String) {
        BUY("buy"), COFFEE_CHOICE(""), BACK("back"),
        FILL("fill"), FILL_WATER(""), FILL_MILK(""), FILL_COFFEE(""), FILL_CUPS(""),
        TAKE("take"),
        REMAINING("remaining"),
        WAITING(""),
        EXIT("exit");

        //  returns a machine state based on its string name
        companion object {
            fun getState(state: String): Machine {
                //  we don't return the empty "" states
                return when (state) {
                    BUY.state -> BUY
                    BACK.state -> BACK
                    FILL.state -> FILL
                    TAKE.state -> TAKE
                    REMAINING.state -> REMAINING
                    else -> EXIT
                }
            }
        }
    }

    //  process user input according to current which machine state
    fun getUserInput(userInput: String) {
        //  waiting at main menu (buy, fill, take ...)
        if (machineState == Machine.WAITING) {
            //update machine state based on user input
            machineState = Machine.getState(userInput)
            when (machineState) {
                Machine.BUY -> buyCoffee()
                Machine.FILL -> fillInventory()
                Machine.TAKE -> takeMoney()
                Machine.REMAINING -> remainingInventory()
                else -> {}   //  Machine.EXIT
            }

        } else {
            //  if back was selected during Buy prompt then go back to main menu
            if (Machine.getState(userInput) == Machine.BACK) {
                showPromptAndChangeState(Prompt.WHAT_TO_DO.text, Machine.WAITING)
            } else {
                // we are in the sub-menus for buy / fill
                val quantity = userInput.toInt()
                when (machineState) {
                    Machine.FILL_WATER -> {
                        fillMachineWith(Inventory.WATER, quantity, Prompt.HOW_MUCH_MILK.text, Machine.FILL_MILK)
                    }

                    Machine.FILL_MILK -> {
                        fillMachineWith(Inventory.MILK, quantity, Prompt.HOW_MUCH_COFFEE.text, Machine.FILL_COFFEE)
                    }

                    Machine.FILL_COFFEE -> {
                        fillMachineWith(Inventory.COFFEE, quantity, Prompt.HOW_MANY_CUPS.text, Machine.FILL_CUPS)
                    }

                    Machine.FILL_CUPS -> {
                        fillMachineWith(Inventory.CUPS, quantity, Prompt.WHAT_TO_DO.text, Machine.WAITING)
                    }

                    //  Machine.COFFEE_CHOICE
                    else -> makeCoffee(Recipe.getById(userInput))
                }
            }
        }
    }

    private fun showPromptAndChangeState(prompt: String, setMachineState: Machine) {
        //  show message prompt to the user
        println(prompt)
        //  switch next machine state to receive amount of inventory to be filled
        machineState = setMachineState
    }

    private fun buyCoffee() {
        showPromptAndChangeState(Prompt.WHAT_TO_BUY.text, Machine.COFFEE_CHOICE)
    }

    private fun updateInventory(coffeeChoice: Recipe) {
        availableWater -= coffeeChoice.water
        availableMilk -= coffeeChoice.milk
        availableCoffee -= coffeeChoice.coffee
        availableCups--
        availableMoney += coffeeChoice.cost
    }

    private fun makeCoffee(coffeeChoice: Recipe) {
        //  check if Inventory allows to make the selected coffee
        val outOfCups = availableCups == 0
        val notEnoughWater = coffeeChoice.water > availableWater
        val notEnoughMilk = coffeeChoice.milk > availableMilk
        val notEnoughCoffee = coffeeChoice.coffee > availableCoffee

        when {
            outOfCups -> {
                showPromptAndChangeState(
                    "${Message.OUT_OF_CUPS.text}${Prompt.WHAT_TO_DO.text}",
                    Machine.WAITING
                )
            }

            notEnoughWater -> {
                showPromptAndChangeState(
                    "${Message.NOT_ENOUGH_WATER.text}${Prompt.WHAT_TO_DO.text}",
                    Machine.WAITING
                )
            }

            notEnoughMilk -> {
                showPromptAndChangeState(
                    "${Message.NOT_ENOUGH_MILK.text}${Prompt.WHAT_TO_DO.text}",
                    Machine.WAITING
                )
            }

            notEnoughCoffee -> {
                showPromptAndChangeState(
                    "${Message.NOT_ENOUGH_COFFEE.text}${Prompt.WHAT_TO_DO.text}",
                    Machine.WAITING
                )
            }

            //  update Inventory after making 1 cup of coffee
            else -> {
                updateInventory(coffeeChoice)
                showPromptAndChangeState(
                    "${Message.MAKING_COFFEE.text}${Prompt.WHAT_TO_DO.text}",
                    Machine.WAITING
                )
            }
        }
    }

    private fun fillInventory() {
        //  first item to fill Inventory is water
        showPromptAndChangeState(Prompt.HOW_MUCH_WATER.text, Machine.FILL_WATER)
    }

    private fun fillMachineWith(item: Inventory, amountFilled: Int, nextPrompt: String, nextMachineState: Machine) {
        //  fill inventory with items
        when (item) {
            Inventory.WATER -> availableWater += amountFilled
            Inventory.MILK -> availableMilk += amountFilled
            Inventory.COFFEE -> availableCoffee += amountFilled
            //  Inventory.CUPS
            else -> availableCups += amountFilled
        }
        //  show prompt and change state
        showPromptAndChangeState(nextPrompt, nextMachineState)
    }

    private fun takeMoney() {
        println(Message.GAVE_MONEY.text)
        availableMoney = 0
        //  main menu (buy, fill, take ...)
        showPromptAndChangeState(Prompt.WHAT_TO_DO.text, Machine.WAITING)
    }

    private fun remainingInventory() {
        //  print current inventory
        println(
            Message.MACHINE_HAS.text +
                    String.format(Message.WATER.text, availableWater)
                    + String.format(Message.MILK.text, availableMilk)
                    + String.format(Message.COFFEE.text, availableCoffee)
                    + String.format(Message.CUPS.text, availableCups)
                    + String.format(Message.MONEY.text, availableMoney)
        )
        //  main menu (buy, fill, take ...)
        showPromptAndChangeState(Prompt.WHAT_TO_DO.text, Machine.WAITING)
    }
}