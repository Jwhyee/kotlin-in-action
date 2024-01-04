package action.junyoung.chapter04.part2

class User(val name: String) {
    var address: String = "unspecified"
        set(value: String) {
            println("""
                Address was changed for $name:
                "$field" -> "$value"
            """.trimIndent())
            address = value
        }
}

interface Member {
    val nickname: String
}

class PrivateMember(override val nickname: String) : Member
class SubscribingMember(val email: String) : Member {
    override val nickname: String
        get() = email.substringBefore('@')
}
class FacebookMember(val accountId: Int) : Member {
    override val nickname: String = getFacebookName(accountId)
}

fun getFacebookName(accountId: Int): String {
    println("호출")
    return "fcMember$accountId"
}

fun main() {

    val user = User("Alice")
    user.address = "Elsenheimerstrasse 47, 80687 Muenchen"

    println(PrivateMember("secret").nickname)
    println(SubscribingMember("youtuber@kotlinlang.org").nickname)

    val member = FacebookMember(1)
    println(member.nickname)
    println(member.nickname)
    println(member.nickname)
}