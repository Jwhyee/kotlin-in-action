package action.junyoung.chapter03.part3.override

fun View.showOff() = println("I'm View!")
fun Button.showOff() = println("I'm Button!")

var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value) {
        this.setCharAt(length - 1, value)
    }