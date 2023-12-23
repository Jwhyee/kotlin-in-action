package action.junyoung.chapter03.part5.user

class User(val id: Int, val name: String, val address: String)

fun User.validateBeforeSave() {
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException(
                "Can't save user $id : empty $fieldName"
            )
        }
    }

    validate(name, "Name")
    validate(address, "Address")
}

fun saveUser(user: User) {
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException(
                "Can't save user ${user.id} : empty $fieldName"
            )
        }
    }
    validate(user.name, "Name")
    validate(user.address, "Address")

    save(user)
}

fun save(user: User) {

}