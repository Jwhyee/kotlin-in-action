package action.junyoung.chapter04.part1

abstract class Animated {
    // 추상 함수 : 구현이 없으며, 하위 클래스에서 반드시 오버라이드 해야 함
    abstract fun animate()

    // 비추상 함수는 기본적으로 final
    fun animateTwice() { }
    // open 변경자를 통해 오버라이드를 허용할 수 있음
    open fun stopAnimating() { }
}