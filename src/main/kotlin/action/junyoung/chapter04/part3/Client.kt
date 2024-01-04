package action.junyoung.chapter04.part3

class Client(val name: String, val postalCode: Int) {
    override fun toString(): String = "Client(name=$name, postalCode=$postalCode)"

    override fun equals(other: Any?): Boolean {
        if(other == null || other !is Client) return false
        return name == other.name && postalCode == other.postalCode
    }
}

fun main() {
    val client1 = Client("Alice", 4122)
    val client2 = Client("Alice", 4122)
    println(client1 == client2)
}