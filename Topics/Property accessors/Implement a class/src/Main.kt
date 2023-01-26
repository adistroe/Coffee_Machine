// write your class here
class LewisCarrollBook {
    var name: String = ""
        set(value) {
            field = value
            println("Now, a book called $field")
        }
        get() {
            println("The name of the book is $field")
            return field
        }

    val author: String = "Lewis Carroll"
        get() {
            println("The author of the book is $field")
            return field
        }

    var price: Int = 0
        set(value) {
            field = value
            println("The new price is $field")
        }
        get() {
            println("Putting a new price...")
            return field
        }
}