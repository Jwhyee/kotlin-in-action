package action.junyoung.chapter04.part1

class Button : Clickable, Focusable {
    override fun click() = println("I was clicked")
    override fun showOff() {
        super<Focusable>.showOff()
        super<Clickable>.showOff()
    }

    fun print() {
        println("I'm Button")
    }
}