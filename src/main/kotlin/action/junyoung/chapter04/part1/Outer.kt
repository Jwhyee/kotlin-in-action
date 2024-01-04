package action.junyoung.chapter04.part1

class Outer {
    inner class Inner {
        fun getOuterReference(): Outer = this@Outer
    }
}